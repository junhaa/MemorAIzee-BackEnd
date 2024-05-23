package memoraize.domain.review.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.photo.entity.Photo;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "place_id")
	private Long id;

	@Column(name = "place_name", nullable = false)
	private String placeName;

	@Column(name = "google_map_place_id")
	private String googleMapId;

	@Column(name = "google_map_photo_url")
	private String photoUrl;

	@OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
	private List<Photo> photoList = new ArrayList<>();

	@OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviewList = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addReview(Review review) {
		this.reviewList.add(review);
		review.setPlace(this);
	}

	public void addPhoto(Photo photo) {
		photoList.add(photo);
		photo.setPlace(this);
	}
}
