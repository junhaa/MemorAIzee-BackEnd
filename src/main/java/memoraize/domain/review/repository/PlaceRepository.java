package memoraize.domain.review.repository;

import memoraize.domain.review.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceName(String placeName);

    List<Place> findAllByPlaceNameContaining(String placeName);

    Optional<Place> findById(Long placeId);
}