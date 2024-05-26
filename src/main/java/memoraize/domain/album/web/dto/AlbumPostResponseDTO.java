package memoraize.domain.album.web.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;

public class AlbumPostResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AddAlbumPostResultDTO {
		private Long albumId;
		private LocalDateTime createdAt;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AlbumPostPreviewResultPageDTO {
		private List<AlbumPostPreviewResultDTO> albums;
		private Integer listSize;
		private Integer totalPage;
		private Long totalElements;
		private Boolean isFirst;
		private Boolean isLast;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AlbumPostPreviewResultDTO {
		private Long albumId;
		private String mainImageUrl;
		private String albumName;
		private LocalDateTime createdAt;

		// TODO HashTag 추가 및 앨범 URL 추가
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AlbumDetailResponseDTO {
		private Long album_id;
		private String album_title;
		private String album_info;
		private Long view_count;
		private Long photo_count;
		private LocalDateTime created_at;
		List<PhotoResponseDTO.PhotoPreviewDTO> photo_list;
	}
}
