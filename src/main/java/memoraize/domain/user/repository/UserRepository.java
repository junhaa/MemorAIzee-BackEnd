package memoraize.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);
	Optional<User> findByRefreshToken(String refreshToken);

	Optional<User> findByLoginTypeAndLoginId(LoginType loginType, String LoginId);
}
