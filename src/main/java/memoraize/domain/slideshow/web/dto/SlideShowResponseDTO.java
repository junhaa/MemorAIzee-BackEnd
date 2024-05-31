package memoraize.domain.slideshow.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SlideShowResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SlideShowPreviewResponseDto implements Comparable<SlideShowPreviewResponseDto> {
		private String userName;
		private String albumName;
		private String slideShowUrl;
		private String mainImageUrl;
		private LocalDateTime createdAt;

		@Override
		public int compareTo(SlideShowPreviewResponseDto other) {
			return other.getCreatedAt().compareTo(this.getCreatedAt());
		}
	}

}
