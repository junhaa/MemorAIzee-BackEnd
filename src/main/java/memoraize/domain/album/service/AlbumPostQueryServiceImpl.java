package memoraize.domain.album.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.converter.AlbumPostConverter;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.enums.SortStatus;
import memoraize.domain.album.exception.AlbumNotExistException;
import memoraize.domain.album.exception.InvalidSortStatusException;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.photo.converter.PhotoConverter;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.repository.PhotoRepository;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumPostQueryServiceImpl implements AlbumPostQueryService {
	private static final Logger log = LogManager.getLogger(AlbumPostQueryServiceImpl.class);
	private final AlbumPostRepository albumPostRepository;
	private final UserRepository userRepository;
	private final PhotoRepository photoRepository;
	private final AlbumPostCommandService albumPostCommandService;

	@Override
	@Transactional
	public AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO getAlbumPage(
		SortStatus sortStatus, Integer page, Integer pageCount) {
		Pageable pageable = toPageable(sortStatus, page, pageCount);
		Page<Album> result;

		// TODO 앨범 공개 범위별로

		result = albumPostRepository.findAll(pageable);
		return AlbumPostConverter.toAlbumPostPreviewResultPageDTO(result);
	}

	@Override
	public Long getAlbumCount(Long userId) {
		return albumPostRepository.countByUserId(userId);
	}

	@Override
	public Page<Album> getUserAlbumPage(Long userId, SortStatus sortStatus, Integer page, Integer pageCount) {
		if (!userRepository.existsById(userId)) {
			throw new UserNotExistException(ErrorStatus._USER_NOT_EXIST);
		}
		Pageable pageable = toPageable(sortStatus, page, pageCount);
		return albumPostRepository.findByUserId(userId, pageable);
	}

	private Pageable toPageable(SortStatus sortStatus, Integer page, Integer pageCount) {
		Sort sort;
		if (sortStatus == SortStatus._LATEST) {
			sort = Sort.by(Sort.Direction.DESC, "createdAt");
		} else if (sortStatus == SortStatus._POPULAR) {
			sort = Sort.by(Sort.Direction.DESC, "viewCount");
		} else {
			throw new InvalidSortStatusException(ErrorStatus._INVALID_SORT_STATUS);
		}
		return PageRequest.of(page - 1, pageCount, sort);
	}

	@Override
	@Transactional
	public AlbumPostResponseDTO.AlbumDetailResponseDTO getAlbumDetail(Long albumId) {
		Album album = albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		List<Photo> photoList = photoRepository.findByAlbumId(album.getId());
		if (photoList.isEmpty()) {
			log.error("앨범 내에 존재하는 사진이 없습니다.");
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
		album.increaseViewCount();
		List<PhotoResponseDTO.PhotoPreviewDTO> photoPreviewDTOList = photoList.stream()
			.map(PhotoConverter::toPhotoPreviewDTO)
			.collect(Collectors.toList());
		return AlbumPostConverter.toAlbumDetailResponseDTO(album, photoPreviewDTOList);
	}
}
