package memoraize.domain.user.service;

import java.util.List;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.web.dto.UserRequestDTO;

public interface UserCommandService {
	User join(UserRequestDTO.SignupRequestDTO request);
	void addUserFollower(User followingUser, Long followerUserId);
	void removeUserFollower(User followingUser, Long followerUserId);

}
