package memoraize.domain.voice.converter;

import memoraize.domain.voice.entity.Voice;
import memoraize.domain.voice.web.dto.VoiceResponseDto;

public class VoiceConverter {
	public static VoiceResponseDto.AddVoiceResponseDTO toAddVoiceResponseDTO(Voice voice) {
		return VoiceResponseDto.AddVoiceResponseDTO.builder()
			.voiceId(voice.getId())
			.createdAt(voice.getCreatedAt())
			.build();
	}
}
