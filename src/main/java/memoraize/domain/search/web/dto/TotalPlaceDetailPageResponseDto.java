package memoraize.domain.search.web.dto;

import lombok.*;
import memoraize.domain.review.entity.Review;

import java.util.List;
import java.util.Optional;

public class TotalPlaceDetailPageResponseDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllPlaceDetailPageResponseDto {
        private PlaceDetailResponseDto.PlaceDetail placeDetail;
        private Optional<List<Review>> reviewList;
    }
}