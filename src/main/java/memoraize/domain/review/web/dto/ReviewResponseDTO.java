package memoraize.domain.review.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddReviewResultDTO {
		private Long reviewId;
		private LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserReviewListResultDTO{
		List<ReviewQueryResultDTO> reviewList;
		Integer listSize;
		Integer totalPage;
		Long totalElements;
		Boolean isFirst;
		Boolean isLast;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReviewQueryResultDTO {
		// User writer;

		// Place place;
		Long reviewId;
		Float star;
		Long viewCount;
		String content;
		String reviewURL;
	}

}
