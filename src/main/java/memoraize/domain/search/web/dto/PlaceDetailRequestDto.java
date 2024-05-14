package memoraize.domain.search.web.dto;

import com.drew.lang.annotations.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceDetailRequestDto {
    @NotNull
    private Long placeId;
}