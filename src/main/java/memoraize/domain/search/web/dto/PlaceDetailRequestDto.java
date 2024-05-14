package memoraize.domain.search.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDetailRequestDto {
    @NotBlank(message = "올바른 장소명을 입력해주세요.")
    private Long placeId;
}