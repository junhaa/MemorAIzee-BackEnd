package memoraize.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.review.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
