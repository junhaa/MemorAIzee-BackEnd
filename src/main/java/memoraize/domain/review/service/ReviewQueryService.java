package memoraize.domain.review.service;

import memoraize.domain.review.entity.Review;
import memoraize.domain.review.web.dto.ReviewResponseDTO;

public interface ReviewQueryService {
	ReviewResponseDTO.UserReviewListResultDTO getUserReview(Long userId, Integer page, Integer pageCount);

}
