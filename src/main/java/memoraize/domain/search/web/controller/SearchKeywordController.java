package memoraize.domain.search.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import memoraize.domain.search.service.SearchKeywordServiceImpl;
import memoraize.domain.search.web.dto.SearchKeywordRequestDTO;
import memoraize.domain.search.web.dto.SearchKeywordResponseDTO;
import memoraize.global.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchKeywordController {

    private final SearchKeywordServiceImpl searchKeywordService;

    @GetMapping("/keyword")
    public ApiResponse<SearchKeywordResponseDTO.SearchResultDTO> searchKeyword(@Valid @RequestBody SearchKeywordRequestDTO keyword) {

        SearchKeywordResponseDTO.SearchResultDTO searchKeywordResponseDTO = searchKeywordService.searchKeyword(keyword);

        return ApiResponse.onSuccess(searchKeywordResponseDTO);
    }
}
