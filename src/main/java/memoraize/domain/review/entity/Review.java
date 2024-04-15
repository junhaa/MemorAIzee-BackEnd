package memoraize.domain.review.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.photo.entity.Photo;
import memoraize.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long id;

	@NotBlank
	@Column(name = "review_context", nullable = false)
	private String context;

	@Column(name = "star", nullable = false)
	private float star;

	@Column(name = "google_review_url")
	private String review_url;

	@ColumnDefault("0")
	@Column(name = "review_view_count", nullable = false)
	private Long viewCount;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "user_id", nullable = false)
	// private User user;

	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewImage> reviewImages = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place_id")
	private Place place;

	public void setGoogleUrl(String url){
		review_url = url;
	}

	// 연관 관계 편의 메서드
	public void addReviewImages(List<ReviewImage> reviewImageList){
		reviewImageList.stream().forEach(image -> {
			this.reviewImages.add(image);
			image.setReview(this);
		});
	}

	public void setPlace(Place place){
		this.place = place;
	}
}
