package memoraize.domain.user.web.dto;

import java.time.LocalDateTime;
import java.util.List;

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

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class FollowerDetailDTO{
		private Long user_id;
		private String user_name;
		private String user_introduction;
		private String user_profile_image_url;
	}

}
