package memoraize.domain.slideshow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideShowChunk {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "slide_show_chunk_id")
	private Long id;

	@Column(name = "slide_show_chunk_public_id", nullable = false)
	private String publicId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memories_id")
	private Memories memories;

	// 연관 관계 편의 메서드
	public void setMemories(Memories memories) {
		this.memories = memories;
	}
}
