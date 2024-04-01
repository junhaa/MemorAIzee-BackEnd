package memoraize.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.photo.entity.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
