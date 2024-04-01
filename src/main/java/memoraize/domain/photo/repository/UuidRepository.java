package memoraize.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.photo.entity.Uuid;

public interface UuidRepository extends JpaRepository<Uuid, Long> {

}
