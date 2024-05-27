package memoraize.domain.voice.web.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class VoiceRequestDto {
	@Getter
	@Setter
	public static class AddVoiceRequestDTO {
		//파일 형식 검증
		@NotNull
		MultipartFile sample;
	}
}
