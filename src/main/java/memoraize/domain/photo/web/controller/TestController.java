package memoraize.domain.photo.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.service.PhotoService;
import memoraize.domain.photo.web.dto.PhotoRequestDTO;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;
import memoraize.global.response.ApiResponse;

/**
 * 사진 저장 테스트용
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

	private final PhotoService photoService;

	@PostMapping("/photo")
	public ApiResponse<List<PhotoResponseDTO.saveReviewResultDTO>> savePhoto(
		@Valid @ModelAttribute PhotoRequestDTO.savePhotoDTO request) {
		log.info("request = {}", request);
		List<PhotoResponseDTO.saveReviewResultDTO> photos = photoService.savePhotoImages(request);
		return ApiResponse.onSuccess(photos);
	}
}
