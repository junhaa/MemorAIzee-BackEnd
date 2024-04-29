package memoraize.domain.user.service;

import java.util.Optional;

import memoraize.domain.user.entity.User;

public interface UserQueryService {

	Optional<User> getUserWithAuthorities(String loginId);
}
