package memoraize.domain.user.service;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.domain.user.web.dto.UserResponseDTO;

public interface UserCommandService {
	User join(UserRequestDTO.SignupRequestDTO request);

	void addUserFollower(User followingUser, Long followerUserId);

	void removeUserFollower(User followingUser, Long followerUserId);

	UserResponseDTO.UpdateUserProfileResponseDTO updateProfile(User user,
		UserRequestDTO.UpdateProfileRequestDTO request);
}
