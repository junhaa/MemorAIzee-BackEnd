package memoraize.global.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

		User user = userRepository.findByLoginId(loginId)
			.orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

		return org.springframework.security.core.userdetails.User.builder()
			.username(user.getLoginId())
			.authorities(user.getAuthorityList()
				.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getRole().toString()))
				.collect(
					Collectors.toSet()))
			.password(user.getPassword())
			.build();
	}
}
