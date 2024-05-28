package memoraize.global.security.oauth.handler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.security.jwt.JwtService;
import memoraize.global.security.oauth.CustomOAuth2User;

@Component
@RequiredArgsConstructor
/**
 * OAuth2 로그인 필터 Success Handler
 */
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
	private static final Logger log = LogManager.getLogger(OAuth2LoginSuccessHandler.class);

	private final JwtService jwtService;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException,
		ServletException {
		try {
			CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
			log.info("OAuth2 Login Success :: Login ID = {}", oAuth2User.getLoginId());
			log.info("Current User Role : {}", oAuth2User.getRole());
			loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
		String accessToken = jwtService.createAccessToken(oAuth2User.getLoginId());
		String refreshToken = jwtService.createRefreshToken();
		response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

		jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtService.updateRefreshToken(oAuth2User.getLoginId(), refreshToken);
	}
}

