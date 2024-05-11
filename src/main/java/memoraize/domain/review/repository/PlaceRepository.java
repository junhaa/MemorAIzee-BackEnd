package memoraize.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import memoraize.domain.review.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {
	Optional<Place> findByPlaceName(String placeName);
	Optional<List<Place>> findAllByPlaceName(String placeNeme);
}