package memoraize.domain.photo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<String, Long> {

}
