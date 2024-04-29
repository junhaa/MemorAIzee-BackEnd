package memoraize.global.security.oauth;

import java.util.Map;

/**
 * Kakao Social Login시 반환되는 Principal
 */
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

	public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getNickname() {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		if (profile == null) {
			return null;
		}

		return (String) profile.get("nickname");
	}

	@Override
	public String getImageUrl() {
		Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");

		if (account == null) {
			return null;
		}

		Map<String, Object> profile = (Map<String, Object>) account.get("profile");

		if (profile == null) {
			return null;
		}

		return (String) profile.get("thumbnail_image_url");
	}
}
