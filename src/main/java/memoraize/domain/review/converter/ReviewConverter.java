package memoraize.domain.review.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import memoraize.domain.review.entity.Review;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.review.web.dto.ReviewResponseDTO;

public class ReviewConverter {
	public static Review toReview(ReviewRequestDTO.createUserReview request){
		return Review.builder()
			.context(request.getContext())
			.star(request.getStar())
			.build();
	}

	public static ReviewResponseDTO.AddReviewResultDTO toAddreviewResultDTO(Review review){
		return ReviewResponseDTO.AddReviewResultDTO.builder()
			.reviewId(review.getId())
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ReviewResponseDTO.UserReviewListResultDTO toUserReviewListResultDTO(Page<Review> reviewPage){
		List<ReviewResponseDTO.ReviewQueryResultDTO> reviewQueryResultDTOList = reviewPage.stream()
			.map(ReviewConverter::toReviewQueryResultDTO).collect(Collectors.toList());

		return ReviewResponseDTO.UserReviewListResultDTO.builder()
			.reviewList(reviewQueryResultDTOList)
			.isFirst(reviewPage.isFirst())
			.isLast(reviewPage.isLast())
			.listSize(reviewPage.getSize())
			.totalElements(reviewPage.getTotalElements())
			.totalPage(reviewPage.getTotalPages())
			.build();

	}

	public static ReviewResponseDTO.ReviewQueryResultDTO toReviewQueryResultDTO(Review review){
		return ReviewResponseDTO.ReviewQueryResultDTO.builder()
			.reviewId(review.getId())
			.star(review.getStar())
			.viewCount(review.getViewCount())
			.content(review.getContext())
			.reviewURL(review.getReview_url())
			.build();
	}


}
