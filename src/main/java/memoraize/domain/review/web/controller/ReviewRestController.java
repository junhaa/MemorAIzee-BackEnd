package memoraize.domain.review.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.service.ReviewCommandService;
import memoraize.domain.review.service.ReviewQueryService;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.review.web.dto.ReviewResponseDTO;
import memoraize.global.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewRestController {

	private final ReviewCommandService reviewCommandService;
	private final ReviewQueryService reviewQueryService;


	@PostMapping()
	public ApiResponse<ReviewResponseDTO.AddReviewResultDTO> addReview(@Valid @ModelAttribute ReviewRequestDTO.createUserReview request){
		Review review = reviewCommandService.addReview(request);
		return ApiResponse.onSuccess(ReviewConverter.toAddreviewResultDTO(review));
	}

	@GetMapping("/{userId}")
	public ApiResponse getReviews(@Valid @PathVariable Long userId, @RequestParam Integer page, @RequestParam Integer pageCount){
		ReviewResponseDTO.UserReviewListResultDTO userReviewList = reviewQueryService.getUserReview(userId, page,
			pageCount);
		return ApiResponse.onSuccess(userReviewList);
	}


}
