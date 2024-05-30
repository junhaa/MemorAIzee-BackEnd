package memoraize.domain.slideshow.service;

import java.util.List;

import memoraize.domain.slideshow.web.dto.SlideShowResponseDTO;

public interface SlideShowQueryService {
	String getMemoriesUrl(Long albumId);

	List<SlideShowResponseDTO.SlideShowPreviewResponseDto> getSlideShowPreview();
}
