package memoraize.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService{

	private final UserRepository userRepository;

	@Override
	public Optional<User> getUserWithAuthorities(String loginId){
		User user = userRepository.findByLoginId(loginId).orElse(null);
		user.getAuthorityList().size();
		return Optional.ofNullable(user);
	}

}
