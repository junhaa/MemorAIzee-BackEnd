package memoraize.domain.album.converter;

import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.enums.AlbumAccess;
import memoraize.domain.album.web.dto.AlbumPostRequestDto;

@Slf4j
public class AlbumPostConverter {
    public static Album toAlbumPost(AlbumPostRequestDto input){
        return Album.builder()
                .albumName(input.getAlbumName())
                .albumInfo(input.getAlbumInfo())
                //.photoURL(new ArrayList<>())
                .albumAccess(AlbumAccess.valueOf(String.valueOf("_"+input.getAlbumAccess().toUpperCase())))
                .build();
    }
}