package memoraize.domain.user.web.dto;

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

}
