package memoraize.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Page<Review> findByUserId(Long userId, Pageable pageable);

	Page<Review> findByPlaceId(Long placeId, Pageable pageable);

	Page<Review> findAll(Pageable pageable);

	List<Review> findByPlaceId(Long placeId);

	List<Review> findByContextContaining(String keyword);
}
