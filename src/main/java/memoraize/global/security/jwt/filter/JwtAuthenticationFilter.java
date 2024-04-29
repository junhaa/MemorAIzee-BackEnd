package memoraize.global.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.repository.UserRepository;
import memoraize.domain.user.service.UserQueryService;
import memoraize.global.security.jwt.JwtService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
/**
 * JWT Authentication 필터
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final UserQueryService userQueryService;

	/**
	 * 로그인 요청 시 JWT 검증 X
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals(CustomUsernamePwdAuthenticationFilter.DEFAULT_LOGIN_REQUEST_URL);
	}

	/**
	 * 	JWT 검증 후
	 * 	요청에 Refresh Token 존재 -> Refresh Token 검증 후 Access Token, Refresh Token 생성
	 * 	요청에 Refresh Token 존재 X -> Access Token 검증
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String refreshToken = jwtService.extractRefreshToken(request)
			.filter(jwtService::isTokenValid)
			.orElse(null);

		if (refreshToken != null) {
			checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
			return;
		}

		if (refreshToken == null) {
			checkAccessTokenAndAuthentication(request, response, filterChain);
		}
	}

	/**
	 * Refresh Token이 유효한 지 검증 후 Access Token 재발급
	 */
	public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
		userRepository.findByRefreshToken(refreshToken)
			.ifPresent(user -> {
				String reIssuedRefreshToken = reIssueRefreshToken(user);
				// AccessToken, RefreshToken response에 전달
				jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getLoginId()),
					reIssuedRefreshToken);
			});
	}

	/**
	 * Refresh Token 재발급
	 */
	private String reIssueRefreshToken(User user) {
		String reIssuedRefreshToken = jwtService.createRefreshToken();
		// 새로운 Refresh Token으로 업데이트
		user.updateRefreshToken(reIssuedRefreshToken);
		userRepository.saveAndFlush(user);
		return reIssuedRefreshToken;
	}

	/**
	 * Access Token 검증 후 인증
	 */
	public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		jwtService.extractAccessToken(request)
			.filter(jwtService::isTokenValid)
			.ifPresent(accessToken -> jwtService.extractLoginId(accessToken)
				.ifPresent(loginId -> userQueryService.getUserWithAuthorities(loginId)
					.ifPresent(this::saveAuthentication)));


		filterChain.doFilter(request, response);
	}

	/**
	 * 검증된 토큰이면 인증
	 */
	public void saveAuthentication(User myUser) {
		String password = myUser.getPassword();
		if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
			password = UUID.randomUUID().toString();
		}

		Set<SimpleGrantedAuthority> authorities = myUser.getAuthorityList().stream().map(authority -> new SimpleGrantedAuthority(authority.getRole().toString())).collect(
			Collectors.toSet());

		UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
			.username(myUser.getLoginId())
			.password(password)
			.authorities(authorities)
			.build();

		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetailsUser, null,
				authorities);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
