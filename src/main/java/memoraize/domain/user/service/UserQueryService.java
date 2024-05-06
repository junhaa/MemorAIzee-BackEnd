package memoraize.domain.user.service;

import java.util.List;
import java.util.Optional;

import memoraize.domain.user.entity.User;

public interface UserQueryService {

	Optional<User> getUserWithAuthorities(String loginId);

	List<User> getFollowerDetailList(User user);

	List<User> getFollowingUsersDetailList(User user);

	Long getFollowerCount(Long userId);

	Long getFollowingCount(Long userId);

	User getUserById(Long userId);

	boolean canFollow(User loginUser, Long followUserId);
}
