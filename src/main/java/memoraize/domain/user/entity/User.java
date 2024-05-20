package memoraize.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.entity.mapping.AlbumLiked;
import memoraize.domain.review.entity.Review;
import memoraize.domain.user.entity.mapping.Follow;
import memoraize.domain.user.enums.LoginType;
import memoraize.domain.user.web.dto.UserRequestDTO;
import memoraize.global.entity.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "login_id", nullable = false)
	private String loginId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "user_introduction")
	private String introduction;

	@Column(name = "phone_number", nullable = true)
	private String phoneNumber;

	@Column(name = "profile_image_url", nullable = true)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(name = "login_type", nullable = false)
	private LoginType loginType;

	@Column(name = "refresh_token")
	private String refreshToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Album> albumList = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AlbumLiked> albumLikedList = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Authority> authorityList = new ArrayList<>();

	@OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followerList = new ArrayList<>();

	@OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followingList = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Review> reviewList = new ArrayList<>();

	// 연관 관계 편의 메서드

	public void addAlbum(Album album) {
		albumList.add(album);
		album.setUser(this);
	}

	public void addAlbumLiked(AlbumLiked albumLiked) {
		albumLikedList.add(albumLiked);
		albumLiked.setUser(this);
	}

	public void addAuthority(Authority authority) {
		authorityList.add(authority);
		authority.setUser(this);
	}

	public void addFollower(Follow follower) {
		followerList.add(follower);
		follower.setFollwer(this);
	}

	public void addFollowing(Follow following) {
		followingList.add(following);
		following.setFolloing(this);
	}

	public void addReview(Review review) {
		reviewList.add(review);
		review.setUser(this);
	}

	public void removeFollower(Follow follower) {
		followerList.remove(follower);
	}

	public void removeFollowing(Follow following) {
		followingList.remove(following);
	}

	// refresh token update
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateProfile(UserRequestDTO.UpdateProfileRequestDTO request, String imageUrl) {
		if (imageUrl != null)
			this.imageUrl = imageUrl;

		if (request.getUserName() != null && !request.getUserName().isEmpty())
			this.userName = request.getUserName();

		if (request.getIntroduction() != null && !request.getIntroduction().isEmpty())
			this.introduction = request.getIntroduction();
	}

}
