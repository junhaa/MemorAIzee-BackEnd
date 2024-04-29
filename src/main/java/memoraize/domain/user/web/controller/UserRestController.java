package memoraize.domain.user.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.converter.UserConverter;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.service.UserCommandServiceImpl;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.domain.user.web.dto.UserResponseDTO;
import memoraize.global.response.ApiResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserRestController {


	private final UserCommandServiceImpl userCommandService;

	/**
	 * 로컬 회원가입
	 */
	@PostMapping("/signup")
	public ApiResponse<UserResponseDTO.SignupResponseDTO> signup(@Valid @RequestBody UserRequestDTO.SignupRequestDTO request, HttpServletResponse response) {
		log.info("request = {}", request);
		User user = userCommandService.join(request);
		return ApiResponse.onSuccess(UserConverter.toSignupResponseDTO(user));
	}



}
