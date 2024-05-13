package memoraize.domain.slideshow.web.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class SlideShowRequestDTO {

	@Getter
	@Setter
	@ToString
	public class CloudinaryNotification {
		private String url;
		private String secure_url;
		private LocalDateTime timestamp;
		private String request_id;
		private String asset_id;
	}

}
