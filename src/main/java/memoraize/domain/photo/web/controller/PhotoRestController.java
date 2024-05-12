package memoraize.domain.photo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import memoraize.domain.photo.service.PhotoQueryService;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;
import memoraize.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photo")
public class PhotoRestController {
	private final PhotoQueryService photoQueryService;

	@GetMapping("/{photoId}")
	public ApiResponse<PhotoResponseDTO.PhotoDetailDTO> getPhotoDetail(@PathVariable(name = "photoId") Long photoId) {
		return ApiResponse.onSuccess(photoQueryService.getPhotoDetail(photoId));
	}
}
