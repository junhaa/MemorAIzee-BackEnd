package memoraize.domain.album.service;

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
import memoraize.domain.album.exception.InvalidSortStatusException;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.user.exception.UserNotExistException;
import memoraize.domain.user.repository.UserRepository;
import memoraize.global.enums.statuscode.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumPostQueryServiceImpl implements AlbumPostQueryService {
	private final AlbumPostRepository albumPostRepository;
	private final UserRepository userRepository;

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
}
