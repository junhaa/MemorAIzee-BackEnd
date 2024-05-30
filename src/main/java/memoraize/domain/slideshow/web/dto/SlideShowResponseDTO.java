package memoraize.domain.slideshow.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SlideShowResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SlideShowPreviewResponseDto {
		private String userName;
		private String albumName;
		private String slideShowUrl;
		private String mainImageUrl;

	}

}
