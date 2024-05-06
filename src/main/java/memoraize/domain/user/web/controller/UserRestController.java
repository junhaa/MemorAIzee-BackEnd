package memoraize.domain.user.web.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.Api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.converter.UserConverter;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.service.UserCommandService;
import memoraize.domain.user.service.UserCommandServiceImpl;
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

	/**
	 * 로컬 회원가입
	 */
	@PostMapping("/signup")
	public ApiResponse<UserResponseDTO.SignupResponseDTO> signup(@Valid @RequestBody UserRequestDTO.SignupRequestDTO request, HttpServletResponse response) {
		log.info("request = {}", request);
		User user = userCommandService.join(request);
		return ApiResponse.onSuccess(UserConverter.toSignupResponseDTO(user));
	}

	/**
	 * 팔로우
	 */
	@PostMapping("/follow/{userId}")
	public ApiResponse<String> follow(@PathVariable(name = "userId") Long followingUserId, @LoginUser User followerUser){
		userCommandService.addUserFollower(followerUser, followingUserId);
		return ApiResponse.onSuccess("팔로잉을 완료했습니다.");
	}

	/**
	 * 팔로우 취소
	 */
	@DeleteMapping("/follow/{userId}")
	public ApiResponse<String> unfollow(@PathVariable(name = "userId") Long followingUserId, @LoginUser User followerUser){
		userCommandService.removeUserFollower(followerUser, followingUserId);
		return ApiResponse.onSuccess("언팔로잉을 완료했습니다.");
	}


	/**
	 * 팔로워 목록
	 */


}
