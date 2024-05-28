package memoraize.domain.slideshow.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import memoraize.domain.slideshow.service.SlideShowQueryService;
import memoraize.domain.slideshow.web.dto.SlideShowRequestDTO;
import memoraize.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slideshow")
public class SlideShowRestController {
	private static final Logger log = LogManager.getLogger(SlideShowRestController.class);
	private final SlideShowQueryService slideShowQueryService;

	@PostMapping("/webhook")
	public ResponseEntity<?> webhook(@RequestBody SlideShowRequestDTO.CloudinaryNotification request) {
		log.info(request.toString());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{albumId}")
	public ApiResponse<String> getMemoriesUrl(@PathVariable(name = "albumId") Long albumId) {
		String memoriesUrl = slideShowQueryService.getMemoriesUrl(albumId);
		return ApiResponse.onSuccess(memoriesUrl);
	}
}
