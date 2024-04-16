package memoraize.domain.photo.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import memoraize.domain.photo.enums.TagCategory;

public interface VisionApiService {
	Map<TagCategory, List<String>> getResultMap();

	void connect(MultipartFile image) throws IOException;

	List<String> detectLabel(MultipartFile image) throws IOException;

	List<String> detectColor(MultipartFile image) throws IOException;
}
