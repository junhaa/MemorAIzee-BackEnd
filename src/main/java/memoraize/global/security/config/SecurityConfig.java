package memoraize.global.security.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import memoraize.domain.user.repository.UserRepository;
import memoraize.domain.user.service.UserQueryService;
import memoraize.global.security.LoginService;
import memoraize.global.security.jwt.JwtService;
import memoraize.global.security.jwt.filter.CustomUsernamePwdAuthenticationFilter;
import memoraize.global.security.jwt.filter.JwtAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import memoraize.global.security.oauth.CustomOAuth2UserService;
import memoraize.global.security.jwt.handler.JwtLoginFailureHandler;
import memoraize.global.security.jwt.handler.JwtLoginSuccessHandler;
import memoraize.global.security.oauth.handler.OAuth2LoginFailureHandler;
import memoraize.global.security.oauth.handler.OAuth2LoginSuccessHandler;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final LoginService loginService;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;
	private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
	private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
	private final JwtLoginFailureHandler jwtLoginFailureHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final UserQueryService userQueryService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) // csrf 보안 사용 X => Rest API
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Token 기반 인증 => session 사용 X
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/login", "/api/user/signup", "/", "/favicon.ico").permitAll() // 허용된 주소
				.anyRequest().authenticated()
			)
			// CORS
			.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOriginPatterns((Collections.singletonList("*")));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization, Authorization-refresh"));
				config.setMaxAge(3600L);
				return config;
			}));
			// OAUTH2 config
			http.oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
				httpSecurityOAuth2LoginConfigurer
					.successHandler(oAuth2LoginSuccessHandler)
					.failureHandler(oAuth2LoginFailureHandler)
					.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService));
			});

		http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
		http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomUsernamePwdAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}


	@Bean
	public CustomUsernamePwdAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
		CustomUsernamePwdAuthenticationFilter customJsonUsernamePasswordLoginFilter
			= new CustomUsernamePwdAuthenticationFilter(objectMapper);
		customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
		customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler);
		customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler);
		return customJsonUsernamePasswordLoginFilter;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService,
			userRepository, userQueryService);
		return jwtAuthenticationFilter;
	}
}
