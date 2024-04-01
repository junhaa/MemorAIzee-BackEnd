package memoraize.domain.album.converter;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.enums.AlbumAccess;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.photo.entity.Photo;

@Slf4j
public class AlbumPostConverter {
    public static Album toAlbumPost(AlbumPostRequestDTO.addAlbumPostDTO request){
        return Album.builder()
                .albumName(request.getAlbumName())
                .albumInfo(request.getAlbumInfo())
                .albumAccess(request.getAlbumAccess())
                .photoImages(new ArrayList<>())
                .isDeleted(false)
                .build();
    }

    public static AlbumPostResponseDTO.addAlbumPostResultDTO toAddAlbumPostResultDTO(Album album){
        return AlbumPostResponseDTO.addAlbumPostResultDTO.builder()
            .albumId(album.getAlbumId())
            .createdAt(album.getCreatedAt())
            .build();
    }
}
