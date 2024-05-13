package memoraize.domain.slideshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.slideshow.entity.Memories;

@Repository
public interface MemoriesRepository extends JpaRepository<Memories, Long> {
}
