package memoraize.domain.slideshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.slideshow.entity.SlideShowChunk;

@Repository
public interface SlideShowChunkRepository extends JpaRepository<SlideShowChunk, Long> {
}
