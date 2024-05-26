package memoraize.domain.photo.service;

import memoraize.domain.photo.web.dto.PhotoResponseDTO;

public interface PhotoQueryService {
	PhotoResponseDTO.PhotoDetailDTO getPhotoDetail(Long photoId);
}
