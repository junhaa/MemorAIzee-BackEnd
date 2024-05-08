package memoraize.domain.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.enums.SortStatus;
import memoraize.domain.album.exception.InvalidSortStatusException;
import memoraize.domain.photo.exception.PlaceNotExistException;
import memoraize.domain.review.converter.ReviewConverter;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.review.web.dto.ReviewResponseDTO;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.enums.statuscode.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryServiceImpl implements ReviewQueryService {
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;

	@Override
	public ReviewResponseDTO.UserReviewListResultDTO getUserReviewByUserId(Long userId, Integer page,
		Integer pageCount) {
		if (!userRepository.existsById(userId))
			throw new UserNotExistException(ErrorStatus._USER_NOT_EXIST);
		Pageable pageable = PageRequest.of(page, pageCount);
		Page<Review> result = reviewRepository.findByUserId(userId, pageable);
		return ReviewConverter.toUserReviewListResultDTO(result);
	}

	@Override
	public ReviewResponseDTO.UserReviewListResultDTO getReviewByPlaceId(Long placeId, Integer page, Integer pageCount) {
		if (!placeRepository.existsById(placeId))
			throw new PlaceNotExistException();
		Pageable pageable = PageRequest.of(page, pageCount);
		Page<Review> result = reviewRepository.findByPlaceId(placeId, pageable);
		return ReviewConverter.toUserReviewListResultDTO(result);
	}

	@Override
	public ReviewResponseDTO.UserReviewListResultDTO getAllReviewsWithSoring(SortStatus sortStatus, Integer page,
		Integer pageCount) {
		Sort sort;
		if (sortStatus == SortStatus._LATEST) {
			sort = Sort.by(Sort.Direction.DESC, "createdAt");
		} else if (sortStatus == SortStatus._POPULAR) {
			sort = Sort.by(Sort.Direction.DESC, "viewCount");
		} else {
			throw new InvalidSortStatusException(ErrorStatus._INVALID_SORT_STATUS);
		}
		Pageable pageable = PageRequest.of(page, pageCount, sort);
		Page<Review> result = reviewRepository.findAll(pageable);
		return ReviewConverter.toUserReviewListResultDTO(result);
	}
}
