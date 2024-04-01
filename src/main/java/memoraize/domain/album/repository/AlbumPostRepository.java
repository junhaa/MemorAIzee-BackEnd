package memoraize.domain.album.repository;

import memoraize.domain.album.entity.Album;
import memoraize.domain.album.web.dto.AlbumPostRequestDto;

import java.util.Optional;

public interface AlbumPostRepository {
    Album albumpost(AlbumPostRequestDto albumPostRequestDto);
    Optional<Album> findByAlbumId(String name);
    Optional<Album> findByIsDeleted();
}
