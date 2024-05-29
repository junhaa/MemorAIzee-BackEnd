package memoraize.domain.voice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.voice.entity.PhotoNarration;

@Repository
public interface PhotoNarrationRepository extends JpaRepository<PhotoNarration, Long> {
	boolean existsByNarrationUrl(String narrationUrl);
}
