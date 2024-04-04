package memoraize.domain.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.entity.Uuid;
import memoraize.domain.photo.repository.UuidRepository;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.entity.ReviewImage;
import memoraize.domain.review.repository.ReviewImageRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.review.web.dto.ReviewRequestDTO;
import memoraize.global.aws.s3.AmazonS3Manager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCommandServiceImpl implements ReviewCommandService {
	private final ReviewRepository reviewRepository;
	private final AmazonS3Manager amazonS3Manager;
	private final ReviewImageRepository reviewImageRepository;
	private final UuidRepository uuidRepository;

	@Override
	@Transactional
	public Review addReview(ReviewRequestDTO.createUserReview request) {
		Review review = ReviewConverter.toReview(request);
		review.addReviewImages(saveReviewImages(request.getImages()));
		//google review 추가 메소드
		return reviewRepository.save(review);
	}

	@Override
	@Transactional
	public List<ReviewImage> saveReviewImages(List<MultipartFile> request){
		List<ReviewImage> reviewImages = new ArrayList<>();

		request.stream().forEach(image ->{
			String uuid = UUID.randomUUID().toString();
			Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

			String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateReviewImageKeyName(savedUuid), image);
			log.info("S3 Saved Image URL = {}", imageUrl);

			ReviewImage reviewImage = ReviewImage.builder()
				.imageUrl(imageUrl)
				.build();

			reviewImages.add(reviewImage);
		});

		return reviewImages;
	}

}
