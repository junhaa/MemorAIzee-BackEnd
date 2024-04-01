package memoraize.domain.album.web.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AlbumPostResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class addAlbumPostResultDTO{
		private Long albumId;
		private LocalDateTime createdAt;
	}
}
