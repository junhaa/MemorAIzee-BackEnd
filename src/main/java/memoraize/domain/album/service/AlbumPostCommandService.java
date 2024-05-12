package memoraize.domain.album.service;

import memoraize.domain.album.entity.Album;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.user.entity.User;

public interface AlbumPostCommandService {

	AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request, User user);

	void deleteAlbum(User user, Long albumId);

	void unlikeAlbum(User user, Long albumId);

	void likeAlbum(User user, Long albumId);

	void increseViewCount(Album album);

}
