package memoraize.domain.review.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import memoraize.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	// Page<Review> findByUserId(Long userId, Pageable pageable);
}
