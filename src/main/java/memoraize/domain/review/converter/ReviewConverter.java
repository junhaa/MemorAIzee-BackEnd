package memoraize.domain.review.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import memoraize.domain.review.entity.Review;
import memoraize.domain.review.entity.ReviewImage;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.review.web.dto.ReviewResponseDTO;
import memoraize.domain.user.entity.User;

public class ReviewConverter {
	public static Review toReview(ReviewRequestDTO.createUserReview request) {
		return Review.builder()
			.context(request.getContext())
			.star(request.getStar())
			.reviewImages(new ArrayList<>())
			.viewCount(0L)
			.build();
	}

	public static ReviewResponseDTO.AddReviewResultDTO toAddreviewResultDTO(Review review) {
		return ReviewResponseDTO.AddReviewResultDTO.builder()
			.reviewId(review.getId())
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ReviewResponseDTO.UserReviewListResultDTO toUserReviewListResultDTO(Page<Review> reviewPage) {
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

	public static ReviewResponseDTO.ReviewQueryResultDTO toReviewQueryResultDTO(Review review) {
		User user = review.getUser();

		return ReviewResponseDTO.ReviewQueryResultDTO.builder()
			.writer(
				ReviewResponseDTO.UserDetailDTO.builder()
					.userid(user.getId())
					.user_image_url(user.getImageUrl())
					.user_name(user.getUserName())
					.build()
			)
			.reviewId(review.getId())
			.star(review.getStar())
			.viewCount(review.getViewCount())
			.content(review.getContext())
			.reviewImages(review.getReviewImages().stream().map(ReviewImage::getImageUrl).collect(Collectors.toList()))
			.build();
	}

}
