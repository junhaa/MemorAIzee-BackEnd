package memoraize.domain.voice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "photo_narration")
public class PhotoNarration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_narration_id")
	private Long id;

	@Column(name = "photo_narration_url", nullable = false)
	private String narrationUrl;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "photo_id")
	private Photo photo;

	// 연관 관계 편의 메서드

	public void changePhoto(Photo photo) {
		this.photo = photo;
	}
}
