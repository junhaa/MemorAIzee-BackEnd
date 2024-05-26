package memoraize.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.user.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	List<Authority> findByUserId(Long userId);
}
