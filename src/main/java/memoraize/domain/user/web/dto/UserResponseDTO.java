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
	public static class SignupResponseDTO {
		private Long userId;
		private LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class UserDetailDTO {
		private Long user_id;
		private String user_name;
		private String user_introduction;
		private String user_profile_image_url;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class UserProfileDTO {
		private Long user_id;
		private String user_name;
		private String image_url;
		private Long album_count;
		private Long follower_count;
		private Long following_count;
		private String user_introduction;
		private boolean can_follow;
	}

}
