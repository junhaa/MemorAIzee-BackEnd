package memoraize.global.security.oauth;

import lombok.Builder;
import lombok.Getter;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import memoraize.global.security.oauth.userinfo.GoogleOAuth2UserInfo;
import memoraize.global.security.oauth.userinfo.KakaoOAuth2UserInfo;
import memoraize.global.security.oauth.userinfo.NaverOAuth2UserInfo;
import memoraize.global.security.oauth.userinfo.OAuth2UserInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;


@Getter
public class OAuthAttributes {

	private String nameAttributeKey; // 고유 값 => loginId
	private OAuth2UserInfo oauth2UserInfo;

	@Builder
	private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
		this.nameAttributeKey = nameAttributeKey;
		this.oauth2UserInfo = oauth2UserInfo;
	}

	/**
	 * LoginType에 맞는 OAuthAttributes 객체 생성
	 */
	public static OAuthAttributes of(LoginType loginType,
		String userNameAttributeName, Map<String, Object> attributes) {
		if (loginType == LoginType.NAVER) {
			return ofNaver(userNameAttributeName, attributes);
		}
		if (loginType == LoginType.KAKAO) {
			return ofKakao(userNameAttributeName, attributes);
		}
		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
			.build();
	}

	public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
			.build();
	}

	public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.nameAttributeKey(userNameAttributeName)
			.oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
			.build();
	}

	public User toEntity(LoginType loginType, OAuth2UserInfo oauth2UserInfo) {
		return User.builder()
			.loginType(loginType)
			.password(UUID.randomUUID().toString())
			.loginId(oauth2UserInfo.getId())
			.authorityList(new ArrayList<>())
			.albumList(new ArrayList<>())
			.albumLikedList(new ArrayList<>())
			.userName(oauth2UserInfo.getNickname())
			.build();
	}
}
