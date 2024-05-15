package memoraize.domain.album.repository;

import memoraize.domain.album.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumPostRepository extends JpaRepository<Album, Long> {
    Page<Album> findAll(Pageable pageable);

    Page<Album> findByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);

    Optional<Album> findById(Long albumId);

    List<Album> findByAlbumNameContaining(String albumName);
}
