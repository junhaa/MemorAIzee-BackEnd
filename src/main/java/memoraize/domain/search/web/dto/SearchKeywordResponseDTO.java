package memoraize.domain.search.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchKeywordResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResultDTO {

        private List<AlbumInfo> albumList;

        private List<UserInfo> userList;

        private List<PlaceInfo> placeList;

        private List<ReviewInfo> reviewList;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlbumInfo {
        private Long id;
        private String albumName;
        private String userName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String userId;
        private String userName;
        private String profileImg;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceInfo {
        private Long id;
        private String placeName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewInfo {
        private Long id;
        private float rate;
        private String context;
    }

}