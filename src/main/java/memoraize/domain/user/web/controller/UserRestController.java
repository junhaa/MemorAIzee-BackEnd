package memoraize.domain.user.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.service.AlbumPostQueryService;
import memoraize.domain.user.converter.UserConverter;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.service.UserCommandService;
import memoraize.domain.user.service.UserQueryService;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.domain.user.web.dto.UserResponseDTO;
import memoraize.global.annotation.LoginUser;
import memoraize.global.response.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserRestController {

	private final UserCommandService userCommandService;
	private final UserQueryService userQueryService;

	private final AlbumPostQueryService albumPostQueryService;

	/**
	 * 로컬 회원가입
	 */
	@PostMapping("/signup")
	public ApiResponse<UserResponseDTO.SignupResponseDTO> signup(
		@Valid @RequestBody UserRequestDTO.SignupRequestDTO request) {
		log.info("request = {}", request);
		User user = userCommandService.join(request);
		return ApiResponse.onSuccess(UserConverter.toSignupResponseDTO(user));
	}

	/**
	 * 팔로우
	 */
	@PostMapping("/follow/{userId}")
	public ApiResponse<String> follow(@PathVariable(name = "userId") Long followingUserId,
		@LoginUser User followerUser) {
		userCommandService.addUserFollower(followerUser, followingUserId);
		return ApiResponse.onSuccess("팔로잉을 완료했습니다.");
	}

	/**
	 * 팔로우 취소
	 */
	@DeleteMapping("/follow/{userId}")
	public ApiResponse<String> unfollow(@PathVariable(name = "userId") Long followingUserId,
		@LoginUser User followerUser) {
		userCommandService.removeUserFollower(followerUser, followingUserId);
		return ApiResponse.onSuccess("언팔로잉을 완료했습니다.");
	}

	/**
	 * 팔로워 목록
	 *  -> 나를 팔로우 중인 사용자
	 */
	@GetMapping("/follow/followers")
	public ApiResponse<List<UserResponseDTO.UserDetailDTO>> followers(@LoginUser User user) {
		List<UserResponseDTO.UserDetailDTO> result = userQueryService.getFollowerDetailList(user)
			.stream()
			.map(UserConverter::toUserDetailDTO)
			.collect(
				Collectors.toList());

		return ApiResponse.onSuccess(result);
	}

	/**
	 * 팔로잉 사용자 목록
	 *  -> 내가 팔로우 중인 사용자 목록
	 */
	@GetMapping("/follow/followingUsers")
	public ApiResponse<List<UserResponseDTO.UserDetailDTO>> followingUsers(@LoginUser User user) {
		List<UserResponseDTO.UserDetailDTO> result = userQueryService.getFollowingUsersDetailList(user)
			.stream()
			.map(UserConverter::toUserDetailDTO)
			.collect(Collectors.toList());
		return ApiResponse.onSuccess(result);
	}

	/**
	 * 사용자 페이지 메인 프로파일
	 */

	@GetMapping("/profile/{userId}")
	public ApiResponse<UserResponseDTO.UserProfileDTO> profile(@LoginUser User user,
		@PathVariable(name = "userId") Long userId) {
		Long albumCount = albumPostQueryService.getAlbumCount(userId);
		Long followerCount = userQueryService.getFollowerCount(userId);
		Long followingCount = userQueryService.getFollowingCount(userId);
		User desUser = userQueryService.getUserById(userId);
		boolean canFollow = false;
		if (user != null) {
			canFollow = !userQueryService.canFollow(user, userId);
		}
		UserResponseDTO.UserProfileDTO result = UserConverter.toUserProfileDTO(desUser, followerCount, followingCount,
			albumCount, canFollow);
		return ApiResponse.onSuccess(result);
	}

	@GetMapping("/profile")
	public ApiResponse<UserResponseDTO.UserProfileDTO> profile(@LoginUser User user) {
		Long albumCount = albumPostQueryService.getAlbumCount(user.getId());
		Long followerCount = userQueryService.getFollowerCount(user.getId());
		Long followingCount = userQueryService.getFollowingCount(user.getId());
		User desUser = userQueryService.getUserById(user.getId());
		boolean canFollow = false;
		UserResponseDTO.UserProfileDTO result = UserConverter.toUserProfileDTO(desUser, followerCount, followingCount,
			albumCount, canFollow);
		return ApiResponse.onSuccess(result);
	}

}
