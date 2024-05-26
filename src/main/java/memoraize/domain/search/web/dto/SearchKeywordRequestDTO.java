package memoraize.domain.search.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchKeywordRequestDTO {

    @NotBlank(message = "키워드를 입력해주세요.")
    private String keyword;
}