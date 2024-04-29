package memoraize.domain.user.service;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.web.dto.UserRequestDTO;

public interface UserCommandService {
	User join(UserRequestDTO.SignupRequestDTO request);
}
