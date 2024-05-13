package memoraize.domain.slideshow.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.album.entity.Album;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Memories {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memories_id")
	private Long id;

	@Column(name = "memories_url", nullable = true)
	private String url;

	@OneToMany(mappedBy = "memories", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SlideShowChunk> slideShowChunkList = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id")
	private Album album;

	//연관 관계 편의 메서드
	public void setAlbum(Album album) {
		this.album = album;
	}

	public void addSlideShowChunk(SlideShowChunk slideShowChunk) {
		slideShowChunkList.add(slideShowChunk);
		slideShowChunk.setMemories(this);
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
