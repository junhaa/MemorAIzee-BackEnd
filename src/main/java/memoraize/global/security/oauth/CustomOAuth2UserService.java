package memoraize.global.security.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.entity.Authority;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.enums.Role;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.security.CustomOAuth2User;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	private static final String NAVER = "naver";
	private static final String KAKAO = "kakao";

	/**
	 * OAuth2UserService를 상속하여
	 * OAuth2User -> CustomOAuth2User 클래스로 반환
	 */
	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("::: OAuth2 로그인 요청 :::");

		// 부모 클래스로 부터 OAuth2User를 받음
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);


		String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 인증 서버 ID
		LoginType loginType = getLoginType(registrationId); // 소셜 로그인 종류
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // 인증 서버에서 제공하는 고유 값
		Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 유저 정보


		// socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
		OAuthAttributes extractAttributes = OAuthAttributes.of(loginType, userNameAttributeName, attributes);

		User createdUser = getUser(extractAttributes, loginType); // getUser() 메소드로 User 객체 생성 후 반환

		log.info("createdUser ={}", createdUser);

		// DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
		return new CustomOAuth2User(
			createdUser.getAuthorityList().stream().map(authority -> new SimpleGrantedAuthority(authority.getRole().toString())).collect(
				Collectors.toList()),
			attributes,
			extractAttributes.getNameAttributeKey(),
			createdUser.getLoginId(),
			createdUser.getAuthorityList().getFirst().getRole()
		);
	}

	/**
	 * registrationId을 통해
	 * 어떤 종류의 소셜 로그인인지 Enum으로 반환
	 */
	private LoginType getLoginType(String registrationId) {
		if(NAVER.equals(registrationId)) {
			return LoginType.NAVER;
		}
		if(KAKAO.equals(registrationId)) {
			return LoginType.KAKAO;
		}
		return LoginType.GOOGLE;
	}

	/**
	 * 인증 서버로 부터 받은 고유 키 값으로 유저 검색
	 * -> 존재하지 않는다면 새로운 유저 생성
	 */
	private User getUser(OAuthAttributes attributes, LoginType loginType) {
		User findUser = userRepository.findByLoginTypeAndLoginId(loginType,
			attributes.getOauth2UserInfo().getId()).orElse(null);

		log.info("findUser = {}", findUser);
		if(findUser == null) {
			return saveUser(attributes, loginType);
		}
		return findUser;
	}

	/**
	 * OAUTH2 최초 로그인 유저 생성
	 */
	private User saveUser(OAuthAttributes attributes, LoginType loginType) {
		User createdUser = attributes.toEntity(loginType, attributes.getOauth2UserInfo());
		createdUser.addAuthority(Authority.builder().role(Role.ROLE_USER).build());
		return userRepository.save(createdUser);
	}
}
