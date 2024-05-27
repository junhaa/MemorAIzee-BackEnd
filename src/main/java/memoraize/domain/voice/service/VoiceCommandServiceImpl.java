package memoraize.domain.voice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.voice.entity.Voice;
import memoraize.domain.voice.repository.VoiceRepository;
import memoraize.domain.voice.web.dto.VoiceRequestDto;
import memoraize.global.elevenlabs.ElevenlabsManager;

@Service
@Transactional
@RequiredArgsConstructor
public class VoiceCommandServiceImpl implements VoiceCommandService {
	private final VoiceRepository voiceRepository;
	private final ElevenlabsManager elevenlabsManager;

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

}
