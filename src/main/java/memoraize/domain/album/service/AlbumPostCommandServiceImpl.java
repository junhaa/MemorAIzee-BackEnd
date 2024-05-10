package memoraize.domain.album.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.converter.AlbumPostConverter;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.photo.service.PhotoCommandService;
import memoraize.domain.user.entity.User;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumPostCommandServiceImpl implements AlbumPostCommandService {

	private final AlbumPostRepository albumPostRepository;
	private final PhotoCommandService photoCommandService;

	@Override
	@Transactional
	public AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request, User user) {
		Album albumPost = AlbumPostConverter.toAlbumPost(request);
		user.addAlbum(albumPost);
		albumPostRepository.save(albumPost);
		photoCommandService.savePhotoImages(request.getImages(), albumPost);

		// 앨범 추가 기능

		return AlbumPostConverter.toAddAlbumPostResultDTO(albumPost);
	}

	@Override
	@Transactional
	public void deleteAlbum(User user, Long albumId) {
		Album album = albumPostRepository.findById(albumId)
			.orElseThrow(() -> new GeneralException(ErrorStatus._ALBUM_NOT_EXIST));

		if (album.getUser().getId() != user.getId()) {
			throw new GeneralException(ErrorStatus._ALBUM_FORBIDEN);
		}

		albumPostRepository.deleteById(albumId);
	}

}
