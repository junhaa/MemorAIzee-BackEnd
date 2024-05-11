package memoraize.domain.search.web.dto;

import lombok.*;
import memoraize.domain.album.entity.Album;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.entity.Review;

import java.util.List;

public class SearchKeywordResponseDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResultDTO{

        private List<Album> albumList;

        private List<SearchUserInfoDTO> userList;

        private List<Place> placeList;

        private List<Review> reviewList;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchUserInfoDTO{

        private Long userId;

        private String userName;

        private String userInstruction;

    }

}