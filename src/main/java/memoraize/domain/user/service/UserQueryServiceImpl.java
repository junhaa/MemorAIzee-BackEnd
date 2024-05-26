package memoraize.domain.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.FollowRepository;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.enums.statuscode.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Override
	public Optional<User> getUserWithAuthorities(String loginId) {
		User user = userRepository.findByLoginId(loginId).orElse(null);
		user.getAuthorityList().size();
		return Optional.ofNullable(user);
	}

	@Override
	public List<User> getFollowerDetailList(User user) {
		return userRepository.findUsersFollowedBy(user.getId());
	}

	@Override
	public List<User> getFollowingUsersDetailList(User user) {
		return userRepository.findUsersFollowingBy(user.getId());
	}

	@Override
	public Long getFollowerCount(Long userId) {
		return followRepository.countByFollowerId(userId);
	}

	@Override
	public Long getFollowingCount(Long userId) {
		return followRepository.countByFollowingId(userId);
	}

	@Override
	public User getUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserNotExistException(ErrorStatus._USER_NOT_EXIST));
	}

	@Override
	public boolean canFollow(User loginUser, Long followUserId) {
		return followRepository.existsByFollowerIdAndFollowingId(followUserId, loginUser.getId());
	}
}
