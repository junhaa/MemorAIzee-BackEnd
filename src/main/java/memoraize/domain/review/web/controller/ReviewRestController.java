package memoraize.domain.review.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import memoraize.domain.album.enums.SortStatus;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.service.ReviewCommandService;
import memoraize.domain.review.service.ReviewQueryService;
import memoraize.domain.review.validation.annotation.Pageable;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.review.web.dto.ReviewResponseDTO;
import memoraize.domain.user.entity.User;
import memoraize.global.annotation.LoginUser;
import memoraize.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewRestController {

	private static final Logger log = LogManager.getLogger(ReviewRestController.class);

	private final ReviewCommandService reviewCommandService;
	private final ReviewQueryService reviewQueryService;

	/**
	 * 리뷰 추가 API
	 */

	@PostMapping()
	public ApiResponse<ReviewResponseDTO.AddReviewResultDTO> addReview(
		@Valid @ModelAttribute ReviewRequestDTO.createUserReview request, @LoginUser User user) {
		Review review = reviewCommandService.addReview(request, user);
		return ApiResponse.onSuccess(ReviewConverter.toAddreviewResultDTO(review));
	}

	/**
	 * 앨범 내부 사진을 이용한 리뷰 추가 API
	 */
	@PostMapping("/{photoId}")
	public ApiResponse<ReviewResponseDTO.AddReviewResultDTO> addReviewWithPhoto(
		@Valid @ModelAttribute ReviewRequestDTO.createUserReview request,
		@PathVariable(name = "photoId") Long photoId,
		@LoginUser User user) {
		Review review = reviewCommandService.addReviewWithPhotoId(request, user, photoId);
		return ApiResponse.onSuccess(ReviewConverter.toAddreviewResultDTO(review));
	}

	/**
	 * 내가 작성한 리뷰 목록 API
	 */
	@GetMapping("/users")
	public ApiResponse<ReviewResponseDTO.UserReviewListResultDTO> getMyReviews(@LoginUser User user,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		page--;
		ReviewResponseDTO.UserReviewListResultDTO userReviewList = reviewQueryService.getUserReviewByUserId(
			user.getId(), page,
			pageCount);
		return ApiResponse.onSuccess(userReviewList);
	}

	/**
	 * 유저가 작성한 리뷰 목록 API
	 */
	@GetMapping("/users/{userId}")
	public ApiResponse<ReviewResponseDTO.UserReviewListResultDTO> getReviewsWithUserId(
		@PathVariable(name = "userId") Long userId,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		page--;
		ReviewResponseDTO.UserReviewListResultDTO userReviewList = reviewQueryService.getUserReviewByUserId(userId,
			page,
			pageCount);
		return ApiResponse.onSuccess(userReviewList);
	}

	/**
	 * 특정 장소에 작성된 리뷰 목록 API
	 */
	@GetMapping("/places/{placeId}")
	public ApiResponse<ReviewResponseDTO.UserReviewListResultDTO> getReviewsWithPlaceId(
		@PathVariable(name = "placeId") Long placeId,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		page--;
		ReviewResponseDTO.UserReviewListResultDTO userReviewList = reviewQueryService.getReviewByPlaceId(placeId, page,
			pageCount);
		return ApiResponse.onSuccess(userReviewList);
	}

	/**
	 * 모든 리뷰 목록 조회 API
	 */
	@GetMapping()
	public ApiResponse<ReviewResponseDTO.UserReviewListResultDTO> getAllReviews(
		@RequestParam(name = "sortStatus") SortStatus sortStatus,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		page--;
		ReviewResponseDTO.UserReviewListResultDTO userReviewDTO = reviewQueryService.getAllReviewsWithSoring(
			sortStatus, page, pageCount);
		return ApiResponse.onSuccess(userReviewDTO);
	}

}
