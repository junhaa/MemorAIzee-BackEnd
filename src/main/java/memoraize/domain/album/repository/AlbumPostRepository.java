package memoraize.domain.album.repository;

import memoraize.domain.album.entity.Album;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumPostRepository extends JpaRepository<Album, Long> {
}
