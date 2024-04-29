package memoraize.domain.user.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class UserResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SignupResponseDTO{
		private Long userId;
		private LocalDateTime createdAt;
	}

}
