package memoraize.domain.photo.web.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
		private String color_code;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LocationDTO {
		private Long place_id;
		private String place_name;
		private double latitude;
		private double longitude;
		private Date date;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PhotoDetailDTO {
		private Long photo_id;
		private String photo_title;
		private String photo_comment;
		private String photo_url;
		private LocationDTO location;
		private List<HashTagResponseDTO> hashTage_list;
		private String photo_color_code;
		private String narration_url;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HashTagResponseDTO {
		private Long hashtag_id;
		private String tag_name;
		private boolean generated_by_ai;
	}

}
