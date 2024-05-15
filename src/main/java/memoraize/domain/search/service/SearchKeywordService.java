package memoraize.domain.search.service;

import memoraize.domain.search.web.dto.SearchKeywordResponseDTO;

public interface SearchKeywordService {
    SearchKeywordResponseDTO.SearchResultDTO searchKeyword(String keyword);
}
