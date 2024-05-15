package memoraize.domain.user.repository;

import memoraize.domain.user.entity.User;
import memoraize.domain.user.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findById(Long userId);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByLoginTypeAndLoginId(LoginType loginType, String LoginId);

    boolean existsByLoginIdAndLoginType(String loginId, LoginType loginType);

    @Query("SELECT u\n"
            + "FROM User u\n"
            + "WHERE u.id IN (\n"
            + "    SELECT f.following.id\n"
            + "    FROM Follow f\n"
            + "    WHERE f.follower.id = :userId\n"
            + ")")
    List<User> findUsersFollowedBy(@Param("userId") Long userId);

    @Query("SELECT u\n"
            + "FROM User u\n"
            + "WHERE u.id IN (\n"
            + "    SELECT f.follower.id\n"
            + "    FROM Follow f\n"
            + "    WHERE f.following.id = :userId\n"
            + ")")
    List<User> findUsersFollowingBy(@Param("userId") Long userId);

    List<User> findByUserNameContaining(String userName);
}
