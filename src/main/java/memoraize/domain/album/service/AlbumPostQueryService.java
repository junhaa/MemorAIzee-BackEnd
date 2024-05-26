package memoraize.domain.album.service;

import org.springframework.data.domain.Page;

import memoraize.domain.album.entity.Album;
import memoraize.domain.album.enums.SortStatus;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;

public interface AlbumPostQueryService {
	AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO getAlbumPage(SortStatus sortStatus, Integer page,
		Integer pageCount);

	Page<Album> getUserAlbumPage(Long userId, SortStatus sortStatus, Integer page, Integer pageCount);

	Long getAlbumCount(Long userId);

	AlbumPostResponseDTO.AlbumDetailResponseDTO getAlbumDetail(Long albumId);
}

