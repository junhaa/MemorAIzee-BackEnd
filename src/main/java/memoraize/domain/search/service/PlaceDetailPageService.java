package memoraize.domain.search.service;

import java.util.List;

import memoraize.domain.review.web.dto.ReviewResponseDTO;
import memoraize.domain.search.web.dto.PlaceDetailResponseDto;

public interface PlaceDetailPageService {
	PlaceDetailResponseDto.PlaceDetail getPlaceDetail(Long placeId);

	List<ReviewResponseDTO.ReviewQueryResultDTO> getAllReviews(Long placeDetailRequestDto);

}
