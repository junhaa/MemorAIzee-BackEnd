package memoraize.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByLoginId(String loginId);
	Optional<User> findByRefreshToken(String refreshToken);

	Optional<User> findByLoginTypeAndLoginId(LoginType loginType, String LoginId);


	@Query("SELECT u\n"
		+ "FROM User u\n"
		+ "WHERE u.id IN (\n"
		+ "    SELECT f.follower.id\n"
		+ "    FROM Follow f\n"
		+ "    WHERE f.following.id = :userId\n"
		+ ")")
	List<User> findUsersFollowedBy(@Param("userId") Long userId);
}
