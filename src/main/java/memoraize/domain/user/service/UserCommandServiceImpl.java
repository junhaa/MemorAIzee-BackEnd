package memoraize.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.converter.UserConverter;
import memoraize.domain.user.entity.Authority;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.entity.mapping.Follow;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.enums.Role;
import memoraize.domain.user.exception.ExistLoginIdException;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.FollowRepository;
import memoraize.domain.user.repository.UserRepository;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.global.enums.statuscode.ErrorStatus;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCommandServiceImpl implements UserCommandService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private final FollowRepository followRepository;

	@Override
	@Transactional
	public User join(UserRequestDTO.SignupRequestDTO request) {
		if (userRepository.existsByLoginIdAndLoginType(request.getLoginId(), LoginType.LOCAL)) {
			log.error("중복되는 로그인 아이디가 있습니다.");
			throw new ExistLoginIdException();
		}

		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = UserConverter.toUser(request, encodedPassword, LoginType.LOCAL);
		log.info("user = {}", user);
		user.addAuthority(Authority.builder().role(Role.ROLE_USER).build());
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public void addUserFollower(User followingUser, Long followerUserId) {
		User followerUser = userRepository.findById(followerUserId)
			.orElseThrow(() -> new UserNotExistException(ErrorStatus._USER_NOT_EXIST));

		// 만약 이미 팔로우 상태라면 return
		if (followRepository.existsByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId()))
			return;

		Follow follow = Follow.builder().build();
		followRepository.save(follow);
		followingUser.addFollowing(follow);
		followerUser.addFollower(follow);
	}

	@Override
	@Transactional
	public void removeUserFollower(User followingUser, Long followerUserId) {
		User followerUser = userRepository.findById(followerUserId)
			.orElseThrow(() -> new UserNotExistException(ErrorStatus._USER_NOT_EXIST));

		// 만약 팔로우 상태가 아니라면 return
		followRepository.findByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId())
			.ifPresent(follow -> {
				follow.getFollower().removeFollower(follow);
				follow.getFollowing().removeFollowing(follow);
				followRepository.delete(follow);
			});
	}

}
