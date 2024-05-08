package memoraize.domain.photo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.album.entity.Album;

public interface PhotoCommandService {
	void savePhotoImages(List<MultipartFile> request, Album album);
}
