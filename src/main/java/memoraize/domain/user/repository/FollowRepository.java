package memoraize.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.user.entity.mapping.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
	boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

	Long countByFollowerId(Long followerId);

	Long countByFollowingId(Long followingId);

	Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
