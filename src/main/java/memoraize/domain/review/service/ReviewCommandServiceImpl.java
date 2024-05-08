package memoraize.domain.review.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.entity.Uuid;
import memoraize.domain.photo.exception.PlaceNotExistException;
import memoraize.domain.photo.repository.UuidRepository;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.entity.ReviewImage;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.domain.user.entity.User;
import memoraize.global.aws.s3.AmazonS3Manager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandServiceImpl implements ReviewCommandService {
	private final ReviewRepository reviewRepository;
	private final PlaceRepository placeRepository;

	private final AmazonS3Manager amazonS3Manager;
	private final UuidRepository uuidRepository;

	@Override
	@Transactional
	public Review addReview(ReviewRequestDTO.createUserReview request, User user) {
		Review review = ReviewConverter.toReview(request);
		review.addReviewImages(saveReviewImages(request.getImages()));
		reviewRepository.save(review);
		user.addReview(review);
		Place place = placeRepository.findById(request.getPlaceId()).orElseThrow(() -> new PlaceNotExistException());
		place.addReview(review);

		return review;
	}

	@Override
	@Transactional
	public List<ReviewImage> saveReviewImages(List<MultipartFile> request) {
		List<ReviewImage> reviewImages = new ArrayList<>();

		request.stream().forEach(image -> {
			String uuid = UUID.randomUUID().toString();
			Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

			byte[] imageBytes = null;
			try {
				imageBytes = image.getBytes();
			} catch (IOException e) {
				log.error("imageByte 추출 중 오류 발생");
				throw new RuntimeException("MultipartFile byte extract error");
			}

			String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateReviewImageKeyName(savedUuid),
				image, imageBytes);
			log.info("S3 Saved Image URL = {}", imageUrl);

			ReviewImage reviewImage = ReviewImage.builder()
				.imageUrl(imageUrl)
				.build();

			reviewImages.add(reviewImage);
		});

		return reviewImages;
	}

}
