package memoraize.domain.review.web.dto;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDTO {
	@Getter
	@Setter
	public static class createUserReview{

		@Size(max = 50)
		@NotBlank
		private String title;

		@NotBlank
		private String context; // 리뷰 내용

		@NotNull
		private Long placeId; // 장소 PK

		@NotNull
		// 소수점 한자리인지 검증
		private Float star; // 별점

		private List<MultipartFile> images; // 리뷰 사진
	}


}
