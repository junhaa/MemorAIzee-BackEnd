package memoraize.domain.photo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "metadata_id")
	private Long id;

	@Column(name = "metadata_latiitude", nullable = false)
	private Double latitude;

	@Column(name = "metadata_longitude", nullable = false)
	private Double longitude;

	@Column(name = "metadata_date", nullable = false)
	private Date date;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "photo_id")
	private Photo photo;

	// 연관 관계 편의 메서드

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
}
