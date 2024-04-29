package memoraize.global.security.jwt.handler;

import java.io.IOException;

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
 * JWT 로그인 필터 Failure Handler
 */
public class JwtLoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws
		IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("JWT 로그인에 실패, 서버 로그를 확인해주세요.");
		log.info("Jwt Login fail :: error = {}", exception.getMessage());
	}
}

