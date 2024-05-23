package memoraize.global.gcp.map.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GooglePhotoResponseDto {
	@JsonProperty("name")
	private String referenceName;

}
