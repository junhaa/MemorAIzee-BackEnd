package memoraize.domain.photo.service;

import java.util.List;

import memoraize.domain.photo.web.dto.PhotoRequestDTO;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;

public interface PhotoService {
	List<PhotoResponseDTO.saveReviewResultDTO> savePhotoImages(PhotoRequestDTO.savePhotoDTO request);
}
