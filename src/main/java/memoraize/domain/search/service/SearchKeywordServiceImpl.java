package memoraize.domain.search.service;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.search.web.dto.SearchKeywordRequestDTO;
import memoraize.domain.search.web.dto.SearchKeywordResponseDTO;
import memoraize.domain.user.entity.User;
import memoraize.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchKeywordServiceImpl implements SearchKeywordService{

    private final AlbumPostRepository albumPostRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewRepository reviewRepository;

    // 검색 메서드
    @Override
    public SearchKeywordResponseDTO.SearchResultDTO searchKeyword(SearchKeywordRequestDTO keyword) {

        Optional<List<Album>> resultAlbumList = albumPostRepository.findByAlbumName(keyword.getKeyword());
        Optional<List<User>> resultUserList = userRepository.findByUserName(keyword.getKeyword());
        Optional<List<Place>> resultPlaceList = placeRepository.findAllByPlaceName(keyword.getKeyword());
        Optional<List<Review>> resultReviewList = reviewRepository.findByReviewTitle(keyword.getKeyword());

        List<SearchKeywordResponseDTO.SearchUserInfoDTO> userInfoDTOList = new ArrayList<>();
        for(User user : resultUserList.get()){
            SearchKeywordResponseDTO.SearchUserInfoDTO userInfoDTO = new SearchKeywordResponseDTO.SearchUserInfoDTO();
            userInfoDTO.setUserId(user.getId());
            userInfoDTO.setUserName(user.getUserName());
            userInfoDTO.setUserInstruction(user.getIntroduction());

            userInfoDTOList.add(userInfoDTO);
        }

        SearchKeywordResponseDTO.SearchResultDTO searchResultDTO = new SearchKeywordResponseDTO.SearchResultDTO();
        searchResultDTO.setAlbumList(resultAlbumList.get());
        searchResultDTO.setUserList(userInfoDTOList);
        searchResultDTO.setPlaceList(resultPlaceList.get());
        searchResultDTO.setReviewList(resultReviewList.get());
        return searchResultDTO;
    }

    // 정렬을 하면 어떤가?
}