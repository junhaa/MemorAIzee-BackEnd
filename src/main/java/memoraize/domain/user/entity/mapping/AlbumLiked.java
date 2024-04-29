package memoraize.domain.user.entity.mapping;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

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
import memoraize.domain.album.entity.Album;
import memoraize.domain.user.entity.User;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumLiked {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_liked_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "album_id")
	private Album album;

	// 연관 관계 편의 메서드
	public void setAlbum(Album album){
		this.album = album;
	}

	public void setUser(User user){
		this.user = user;
	}
}
