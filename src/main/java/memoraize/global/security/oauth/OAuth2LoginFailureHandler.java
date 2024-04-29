package memoraize.global.security.oauth;

import java.io.IOException;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
/**
 * OAuth2 로그인 필터 Failure Handler
 */
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws
		IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("소셜 로그인 실패, 서버 로그를 확인해주세요.");
		log.info("OAuth2 Login fail :: error = {}", exception.getMessage());
	}
}

