package memoraize.domain.photo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.photo.enums.TagCategory;

public interface VisionApiService {

	Map<TagCategory, List<String>> connect(MultipartFile image, byte[] imageBytes) throws IOException;

	List<String> detectLabel(MultipartFile image, byte[] imageBytes) throws IOException;

	List<String> detectColor(MultipartFile image, byte[] imageBytes) throws IOException;
}
