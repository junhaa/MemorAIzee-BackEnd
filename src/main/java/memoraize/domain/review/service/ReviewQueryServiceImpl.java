package memoraize.domain.review.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.review.web.dto.ReviewResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService{
	private final ReviewRepository reviewRepository;

	@Override
	@Transactional
	public ReviewResponseDTO.UserReviewListResultDTO getUserReview(Long userId, Integer page, Integer pageCount) {
		Pageable pageable = PageRequest.of(page, pageCount);
		// Page<Review> result = reviewRepository.findByUserId(userId, pageable);
		// return ReviewConverter.toUserReviewListResultDTO(result);
		return null;
	}
}
