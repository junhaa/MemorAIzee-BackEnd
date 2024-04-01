package memoraize.domain.photo.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PhotoResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class saveReviewResultDTO {
		private Long imageId;
		private String imageUrl;
		private LocalDateTime createdAt;
	}
}
