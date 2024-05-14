package memoraize.domain.search.service;

import memoraize.domain.review.entity.Review;
import memoraize.domain.search.web.dto.PlaceDetailRequestDto;
import memoraize.domain.search.web.dto.PlaceDetailResponseDto;

import java.util.List;
import java.util.Optional;

public interface PlaceDetailPageService {
    PlaceDetailResponseDto.PlaceDetail getPlaceDetail(PlaceDetailRequestDto placeDetailRequestDto);

    Optional<List<Review>> getAllReviews(PlaceDetailRequestDto placeDetailRequestDto);

}
