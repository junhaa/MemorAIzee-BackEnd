package memoraize.domain.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.search.web.dto.SearchKeywordResponseDTO;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchKeywordServiceImpl implements SearchKeywordService {

	private final AlbumPostRepository albumPostRepository;
	private final UserRepository userRepository;
	private final PlaceRepository placeRepository;
	private final ReviewRepository reviewRepository;

	// 검색 메서드
	@Override
	public SearchKeywordResponseDTO.SearchResultDTO searchKeyword(String keyword) {

		List<Album> resultAlbumList = albumPostRepository.findByAlbumNameContaining(keyword);
		List<User> resultUserList = userRepository.findByUserNameContaining(keyword);
		List<Place> resultPlaceList = placeRepository.findAllByPlaceNameContaining(keyword);
		List<Review> resultReviewList = reviewRepository.findByContextContaining(keyword);

		List<SearchKeywordResponseDTO.AlbumInfo> albumInfo = new ArrayList<>();
		for (Album album : resultAlbumList) {
			albumInfo.add(SearchKeywordResponseDTO.AlbumInfo.builder()
				.id(album.getId())
				.albumName(album.getAlbumName())
				.userName(album.getUser().getUserName())
				.mainImg(album.getPhotoImages().get(0).getImageUrl())
				.build());
		}

		List<SearchKeywordResponseDTO.UserInfo> userInfo = new ArrayList<>();
		for (User user : resultUserList) {
			userInfo.add(SearchKeywordResponseDTO.UserInfo.builder()
				.id(user.getId())
				.userId(user.getLoginId())
				.userName(user.getUserName())
				.profileImg(user.getImageUrl())
				.build());
		}

		List<SearchKeywordResponseDTO.PlaceInfo> placeInfo = new ArrayList<>();
		for (Place place : resultPlaceList) {
			placeInfo.add(SearchKeywordResponseDTO.PlaceInfo.builder()
				.id(place.getId())
				.placeName(place.getPlaceName())
				.build());
		}

		List<SearchKeywordResponseDTO.ReviewInfo> reviewInfo = new ArrayList<>();
		for (Review review : resultReviewList) {
			reviewInfo.add(SearchKeywordResponseDTO.ReviewInfo.builder()
				.id(review.getId())
				.rate(review.getStar())
				.context(review.getContext())
				.build());
		}

		return SearchKeywordResponseDTO.SearchResultDTO.builder()
			.albumList(albumInfo)
			.userList(userInfo)
			.placeList(placeInfo)
			.reviewList(reviewInfo)
			.build();
	}
}
