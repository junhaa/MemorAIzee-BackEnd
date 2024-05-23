package memoraize.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.photo.entity.Photo;

public interface PhotoCommandService {
	Photo savePhotoImages(MultipartFile image);
}
