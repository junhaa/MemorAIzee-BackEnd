package memoraize.domain.review.web.dto;


import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDTO {
	@Getter
	@Setter
	public static class createUserReview{

		@NotBlank
		private String context; // 리뷰 내용

		@NotNull
		// 소수점 한자리인지 검증
		private Long placeId; // 장소 PK

		@NotNull
		private Float star; // 별점
	}


}
