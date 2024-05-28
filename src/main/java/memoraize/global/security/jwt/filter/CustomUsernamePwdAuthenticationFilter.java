package memoraize.global.security.jwt.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT 로그인 POST 요청 왔을 때 인증 필터
 */
public class CustomUsernamePwdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
	private static final String HTTP_METHOD = "POST";
	private static final String CONTENT_TYPE = "application/json";
	private static final String LOGINID_KEY = "loginId";
	private static final String PASSWORD_KEY = "password";
	private static final Logger log = LogManager.getLogger(CustomUsernamePwdAuthenticationFilter.class);

	private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
		new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

	private final ObjectMapper objectMapper;

	public CustomUsernamePwdAuthenticationFilter(ObjectMapper objectMapper) {
		super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 기존 formlogin 형태를 변경
		this.objectMapper = objectMapper;
	}

	/**
	 * JWT 로컬 로그인 인증 과정
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException,
		IOException,
		ServletException {
		// 지원하는 ContentType이 아닌 경우
		if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException(
				"Authentication Content-Type not supported: " + request.getContentType());
		}

		// request body에서 로그인 ID와 비밀번호 추출
		String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

		Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);
		String loginId = usernamePasswordMap.get(LOGINID_KEY);
		String password = usernamePasswordMap.get(PASSWORD_KEY);

		log.info("JWT Local Login ::: loginId = {}, password = {}", loginId, password);

		//principal 과 credentials 전달
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginId, password);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
