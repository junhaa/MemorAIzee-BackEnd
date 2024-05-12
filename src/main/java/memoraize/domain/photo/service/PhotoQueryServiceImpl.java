package memoraize.domain.photo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.photo.converter.PhotoConverter;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.entity.PhotoHashTag;
import memoraize.domain.photo.exception.PhotoNotExistException;
import memoraize.domain.photo.repository.PhotoHashtagRepository;
import memoraize.domain.photo.repository.PhotoRepository;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoQueryServiceImpl implements PhotoQueryService {
	private final PhotoRepository photoRepository;
	private final PhotoHashtagRepository photoHashtagRepository;

	@Override
	public PhotoResponseDTO.PhotoDetailDTO getPhotoDetail(Long photoId) {
		Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotExistException());
		List<PhotoHashTag> photoHashTagList = photoHashtagRepository.findByPhotoId(photo.getId());
		return PhotoConverter.toPhotoDetailDTO(photo, photoHashTagList);
	}
}
