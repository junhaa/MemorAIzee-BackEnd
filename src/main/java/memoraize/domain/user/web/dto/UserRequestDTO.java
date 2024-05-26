package memoraize.domain.user.web.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {
	@Getter
	@Setter
	public static class SignupRequestDTO {
		@NotNull
		private String loginId;
		@NotNull
		private String password;
		private String userName;
		private String phoneNumber;
	}

	@Getter
	@Setter
	public static class UpdateProfileRequestDTO {
		private MultipartFile profileImage;
		private String userName;
		private String introduction;
	}

}
