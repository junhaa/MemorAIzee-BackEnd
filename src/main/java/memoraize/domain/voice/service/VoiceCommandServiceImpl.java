package memoraize.domain.voice.service;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.repository.PhotoRepository;
import memoraize.domain.user.entity.User;
import memoraize.domain.voice.entity.PhotoNarration;
import memoraize.domain.voice.entity.Voice;
import memoraize.domain.voice.repository.PhotoNarrationRepository;
import memoraize.domain.voice.repository.VoiceRepository;
import memoraize.domain.voice.web.dto.VoiceRequestDto;
import memoraize.global.aws.s3.AmazonS3Manager;
import memoraize.global.elevenlabs.ElevenlabsManager;
import memoraize.global.util.FileConverter;

@Service
@Transactional
@RequiredArgsConstructor
public class VoiceCommandServiceImpl implements VoiceCommandService {

	private final PhotoRepository photoRepository;
	private final VoiceRepository voiceRepository;
	private final ElevenlabsManager elevenlabsManager;

	private final PhotoNarrationRepository photoNarrationRepository;

	private final AmazonS3Manager amazonS3Manager;

	@Override
	public Voice addVoice(VoiceRequestDto.AddVoiceRequestDTO request, User user) {
		String keyId = elevenlabsManager.addVoice(user.getUserName(), request.getSample());
		Optional<Voice> voiceOptional = voiceRepository.findByUserId(user.getId());
		Voice voice;
		if (voiceOptional.isPresent()) {
			voice = voiceOptional.get();
			elevenlabsManager.deleteVoice(voice.getVoiceKey());
		} else {
			voice = Voice.builder().build();
			user.updateVoice(voice);
		}

		voice.updateVoiceKey(keyId);
		return voiceRepository.save(voice);
	}

	@Override
	@Async
	public void createPhotoNarrationAndSave(Photo photo, String voiceKey) {
		File file = elevenlabsManager.elevenLabsTTS(photo.getComment(), voiceKey);

		String fileName = UUID.randomUUID().toString();

		String savedUrl = amazonS3Manager.uploadFile(amazonS3Manager.generatePhotoNarrationKeyName(fileName),
			FileConverter.toMultipartFile(file, fileName));

		PhotoNarration photoNarration = PhotoNarration.builder()
			.narrationUrl(savedUrl)
			.build();

		photo.setPhotoNarration(photoNarration);

		photoNarrationRepository.save(photoNarration);
	}

}
