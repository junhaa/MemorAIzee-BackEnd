package memoraize.domain.review.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import memoraize.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByReviewTitle(String title);
	Page<Review> findByUserId(Long userId, Pageable pageable);

	Page<Review> findByPlaceId(Long placeId, Pageable pageable);

	Page<Review> findAll(Pageable pageable);
}
