package memoraize.domain.album.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumPostRequestDto {
    private String albumName;
    private String albumInfo;
    private String albumAccess;
    //private User user;
    //private List<MultipartFile> images;
    private Boolean isDeleted;
}