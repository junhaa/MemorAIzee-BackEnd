package memoraize.domain.photo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoHashTag extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "photo_hashtag_id")
	private Long Id;


	@Column(name = "photo_hashtag_name", nullable = false)
	private String tagName;

	@Column(name = "generated_by_ai", nullable = false)
	private boolean genByAI;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "photo_id")
	private Photo photo;
}
