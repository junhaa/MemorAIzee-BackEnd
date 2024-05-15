package memoraize.domain.search.web.controller;

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
    public ApiResponse<SearchKeywordResponseDTO.SearchResultDTO> searchKeyword(@RequestParam String keyword) {

        SearchKeywordResponseDTO.SearchResultDTO searchKeywordResponseDTO = searchKeywordService.searchKeyword(keyword);

        return ApiResponse.onSuccess(searchKeywordResponseDTO);
    }

    @GetMapping("/placeDetail/{placeId}")
    public ApiResponse<TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto> getPlaceDetail(@PathVariable(name = "placeId") Long placeId) { // pathVariable로 바꾸기

        TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto allPlaceDetailPageResponseDto = new TotalPlaceDetailPageResponseDto.AllPlaceDetailPageResponseDto();
        allPlaceDetailPageResponseDto.setPlaceDetail(placeDetailPageService.getPlaceDetail(placeId));
        allPlaceDetailPageResponseDto.setReviewList(placeDetailPageService.getAllReviews(placeId));

        return ApiResponse.onSuccess(allPlaceDetailPageResponseDto);
    }
}
