package memoraize.global.security.jwt.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.repository.UserRepository;
import memoraize.domain.user.web.dto.UserResponseDTO;
import memoraize.global.response.ApiResponse;
import memoraize.global.security.jwt.JwtService;

@Slf4j
@Component
@RequiredArgsConstructor
/**
 * JWT 로그인 필터 Success Handler
 */
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtService jwtService;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	/**
	 * 인증에 성공하면 Access Token과 Refresh Token을 생성한 후 반환
	 */
	@Override
	@Transactional(readOnly = true)
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		UserDetails principal = (UserDetails)authentication.getPrincipal();
		String loginId = principal.getUsername();
		log.info("Jwt Login Success :: Login ID = {}", loginId);
		response.setContentType("application/json;charset=UTF-8");

		User user = userRepository.findByLoginTypeAndLoginId(LoginType.LOCAL, loginId).orElse(null);
		response.getWriter().write(objectMapper.writeValueAsString(
			ApiResponse.onSuccess(UserResponseDTO.LoginResponseDTO.builder().user_id(user.getId()).build())));
		loginSuccess(response, loginId);
	}

	private void loginSuccess(HttpServletResponse response, String loginId) throws IOException {
		String accessToken = jwtService.createAccessToken(loginId);
		String refreshToken = jwtService.createRefreshToken();
		response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(loginId, refreshToken);
	}
}
