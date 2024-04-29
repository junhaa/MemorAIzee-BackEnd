package memoraize.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.user.converter.UserConverter;
import memoraize.domain.user.entity.Authority;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.enums.Role;
import memoraize.domain.user.repository.UserRepository;
import memoraize.domain.user.web.dto.UserRequestDTO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCommandServiceImpl implements UserCommandService{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public User join(UserRequestDTO.SignupRequestDTO request){
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		User user = UserConverter.toUser(request, encodedPassword, LoginType.LOCAL);
		log.info("user = {}", user);
		user.addAuthority(Authority.builder().role(Role.ROLE_USER).build());
		return userRepository.save(user);
	}

}
