package memoraize.domain.search.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import memoraize.domain.search.service.PlaceDetailPageServiceImpl;
import memoraize.domain.search.service.SearchKeywordServiceImpl;
import memoraize.domain.search.web.dto.*;
import memoraize.global.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchKeywordController {

    private final SearchKeywordServiceImpl searchKeywordService;
    private final PlaceDetailPageServiceImpl placeDetailPageService;

    @GetMapping("/keyword")
    public ApiResponse<SearchKeywordResponseDTO.SearchResultDTO> searchKeyword(@Valid @RequestBody SearchKeywordRequestDTO keyword) {

        SearchKeywordResponseDTO.SearchResultDTO searchKeywordResponseDTO = searchKeywordService.searchKeyword(keyword);

        return ApiResponse.onSuccess(searchKeywordResponseDTO);
    }

    @GetMapping("/placeDetail")
    public ApiResponse<TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto> getPlaceDetail(@Valid @RequestBody PlaceDetailRequestDto placeId) {

        TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto allPlaceDetailPageResponseDto = new TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto();
        allPlaceDetailPageResponseDto.setPlaceDetail(placeDetailPageService.getPlaceDetail(placeId));
        allPlaceDetailPageResponseDto.setReviewList(placeDetailPageService.getAllReviews(placeId));

        return ApiResponse.onSuccess(allPlaceDetailPageResponseDto);
    }
}
