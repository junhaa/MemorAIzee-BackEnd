package memoraize.domain.album.web.dto;

import lombok.*;

import java.util.List;

public class GoogleMapsPlatformResponseDTO {

    // 한 앨범에 대한 일별 메타데이터 목록을 담은 리스트
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WayPointsList{
        private List<GetMedataByDate> wayPointsList;
    }

    // 일별 메타데이터 목록을 담은 리스트
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GetMedataByDate {
        private List<GetMetadataList> metadataByDate;
    }

    // 메타데이터
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMetadataList {
        private double lat;
        private double lng;
        private String placeTitle;
    }
}