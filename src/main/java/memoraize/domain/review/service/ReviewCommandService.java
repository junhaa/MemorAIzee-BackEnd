package memoraize.domain.review.service;

import memoraize.domain.review.entity.Review;
import memoraize.domain.review.web.dto.ReviewRequestDTO;

public interface ReviewCommandService {
	public Review addReview(ReviewRequestDTO.createUserReview request);
}
