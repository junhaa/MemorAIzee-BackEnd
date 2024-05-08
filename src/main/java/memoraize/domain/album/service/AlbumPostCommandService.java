package memoraize.domain.album.service;

import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.user.entity.User;

public interface AlbumPostCommandService {

	AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request, User user);

}
