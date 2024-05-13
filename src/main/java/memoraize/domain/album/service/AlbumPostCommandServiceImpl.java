package memoraize.domain.album.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.converter.AlbumPostConverter;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.entity.mapping.AlbumLiked;
import memoraize.domain.album.exception.AlbumNotExistException;
import memoraize.domain.album.repository.AlbumLikedRepository;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.photo.service.PhotoCommandService;
import memoraize.domain.slideshow.service.SlideShowCommandService;
import memoraize.domain.user.entity.User;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumPostCommandServiceImpl implements AlbumPostCommandService {

	private final AlbumPostRepository albumPostRepository;

	private final AlbumLikedRepository albumLikedRepository;
	private final PhotoCommandService photoCommandService;
	private final SlideShowCommandService slideShowCommandService;

	@Override
	@Transactional
	public AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request, User user) {
		Album albumPost = AlbumPostConverter.toAlbumPost(request);
		user.addAlbum(albumPost);
		albumPostRepository.save(albumPost);
		photoCommandService.savePhotoImages(request.getImages(), albumPost);

		slideShowCommandService.makeSlideShow(albumPost);
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

	@Override
	@Transactional
	public void likeAlbum(User user, Long albumId) {
		Album album = albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		if (albumLikedRepository.findByUserIdAndAlbumId(user.getId(), albumId).isPresent())
			return;
		AlbumLiked albumLiked = AlbumLiked.builder()
			.album(album)
			.user(user)
			.build();

		albumLikedRepository.save(albumLiked);
	}

	@Override
	@Transactional
	public void unlikeAlbum(User user, Long albumId) {
		albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		albumLikedRepository.findByUserIdAndAlbumId(user.getId(), albumId).ifPresent(albumLiked -> {
			albumLikedRepository.delete(albumLiked);
		});
	}

}
