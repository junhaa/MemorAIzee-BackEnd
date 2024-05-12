package memoraize.domain.photo.web.dto;

import java.time.LocalDateTime;
import java.util.Date;

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

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PhotoPreviewDTO {
		private Long photo_id;
		private String photo_url;
		private LocationDTO location;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LocationDTO {
		private Long place_id;
		private String placeName;
		private double latitude;
		private double longitude;
		private Date date;
	}

}
