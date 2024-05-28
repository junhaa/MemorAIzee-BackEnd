package memoraize.domain.voice.service;

import memoraize.domain.photo.entity.Photo;
import memoraize.domain.user.entity.User;
import memoraize.domain.voice.entity.Voice;
import memoraize.domain.voice.web.dto.VoiceRequestDto;

public interface VoiceCommandService {
	Voice addVoice(VoiceRequestDto.AddVoiceRequestDTO request, User user);

	void createPhotoNarrationAndSave(Photo photo, String voiceKey);
}
