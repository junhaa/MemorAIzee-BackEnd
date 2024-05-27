package memoraize.domain.voice.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class VoiceResponseDto {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddVoiceResponseDTO {
		private Long voiceId;
		private LocalDateTime createdAt;
	}
}
