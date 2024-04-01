package memoraize.domain.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.review.web.dto.ReviewRequestDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandServiceImpl implements ReviewCommandService {
	private final ReviewRepository reviewRepository;

	@Override
	@Transactional
	public Review addReview(ReviewRequestDTO.createUserReview request) {
		Review review = ReviewConverter.toReview(request);
		//google review 추가 메소드
		return reviewRepository.save(review);
	}

}
