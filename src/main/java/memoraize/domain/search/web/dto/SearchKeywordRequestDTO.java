package memoraize.domain.search.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchKeywordRequestDTO {

    @NotEmpty(message = "키워드를 입력해주세요.")
    private String keyword;
}