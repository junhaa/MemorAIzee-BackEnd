package memoraize.domain.search.service;

import memoraize.domain.search.web.dto.SearchKeywordRequestDTO;
import memoraize.domain.search.web.dto.SearchKeywordResponseDTO;

public interface SearchKeywordService {
    SearchKeywordResponseDTO.SearchResultDTO searchKeyword(SearchKeywordRequestDTO keyword);
}
