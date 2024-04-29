package memoraize.global.security.oauth.userinfo;

import java.util.Map;

/**
 * Google Social Login시 반환되는 정보
 */
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return (String) attributes.get("sub");
	}

	@Override
	public String getNickname() {
		return (String) attributes.get("name");
	}

	@Override
	public String getImageUrl() {
		return (String) attributes.get("picture");
	}
}
