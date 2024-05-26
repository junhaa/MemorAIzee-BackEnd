package memoraize.domain.album.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.album.entity.mapping.AlbumLiked;

@Repository
public interface AlbumLikedRepository extends JpaRepository<AlbumLiked, Long> {
	Optional<AlbumLiked> findByUserIdAndAlbumId(Long userId, Long albumid);
}
