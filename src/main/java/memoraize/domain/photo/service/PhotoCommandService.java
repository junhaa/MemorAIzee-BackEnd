package memoraize.domain.photo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.photo.entity.Photo;

public interface PhotoCommandService {
	List<Photo> savePhotoImages(List<MultipartFile> request);
}
