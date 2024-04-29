package memoraize.global.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import lombok.Getter;
import lombok.ToString;
import memoraize.domain.user.enums.Role;

@Getter
@ToString
/**
 * 기존의 DefaultOAuth2User에 LoginID, role 추가
 */
public class CustomOAuth2User extends DefaultOAuth2User {
	private String loginId;
	private Role role;
	public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
		Map<String, Object> attributes, String nameAttributeKey,
		String loginId, Role role) {
		super(authorities, attributes, nameAttributeKey);
		this.loginId = loginId;
		this.role = role;
	}
}
