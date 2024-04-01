package memoraize.domain.album.service;

import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;

public interface AlbumPostCommandService {
	AlbumPostResponseDTO.addAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request);

}
