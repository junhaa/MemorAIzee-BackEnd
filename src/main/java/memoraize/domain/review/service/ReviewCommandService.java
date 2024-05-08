package memoraize.domain.review.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.review.entity.Review;
import memoraize.domain.review.entity.ReviewImage;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.user.entity.User;

public interface ReviewCommandService {
	Review addReview(ReviewRequestDTO.createUserReview request, User user);

	List<ReviewImage> saveReviewImages(List<MultipartFile> request);

	Review addReviewWithPhotoId(ReviewRequestDTO.createUserReview request, User user, Long photoId);
}
