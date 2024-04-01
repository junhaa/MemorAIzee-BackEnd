package memoraize.domain.album.web.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import memoraize.domain.album.enums.AlbumAccess;
import memoraize.domain.album.validation.annotation.ListNotBlank;

public class AlbumPostRequestDTO {

    @Getter
    @Setter
    public static class addAlbumPostDTO{
        private String albumName;
        private String albumInfo;
        //private User user;
        @ListNotBlank
        private List<MultipartFile> images;
        private AlbumAccess albumAccess;
        private Boolean isDeleted;
    }

}
