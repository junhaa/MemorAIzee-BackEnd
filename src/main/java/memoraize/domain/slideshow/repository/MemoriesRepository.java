package memoraize.domain.slideshow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import memoraize.domain.slideshow.entity.Memories;

@Repository
public interface MemoriesRepository extends JpaRepository<Memories, Long> {
	List<Memories> findAllByOrderByIdDesc();
}
