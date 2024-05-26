package memoraize.domain.album.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import memoraize.domain.album.entity.mapping.AlbumLiked;
import memoraize.domain.album.enums.AlbumAccess;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.slideshow.entity.Memories;
import memoraize.domain.user.entity.User;
import memoraize.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Album extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private Long id;

	@NotBlank
	@Column(name = "albumName", nullable = false)
	private String albumName;

	@NotBlank
	@Column(name = "albumInfo", nullable = false)
	private String albumInfo;

	// 사진 url을 받기 위해 .. photo entity가 따로 있어야 하는데 아직 없어서 임시로 string으로..
	// 윤석오빠가 url, metadata를 photo 객체에 담아서 나한테 주면 내가 그걸 map api에 넣는다..

	@OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
	private List<Photo> photoImages = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private AlbumAccess albumAccess;

	@ColumnDefault("0")
	@Column(name = "view_count", nullable = false)
	private Long viewCount;

	@Column(name = "isDeleted", nullable = false)
	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
	private List<AlbumLiked> albumLikedList = new ArrayList<>();

	@OneToOne(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
	private Memories memories;

	// 연관 관계 편의 메서드
	public void addAlbumLiked(AlbumLiked albumLiked) {
		albumLikedList.add(albumLiked);
		albumLiked.setAlbum(this);
	}

	public void setMemories(Memories memories) {
		this.memories = memories;
		memories.setAlbum(this);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void addPhoto(Photo photo) {
		photoImages.add(photo);
		photo.setAlbum(this);
	}

	public void increaseViewCount() {
		this.viewCount++;
	}
}
