package memoraize.domain.slideshow.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.slideshow.web.dto.SlideShowRequestDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slideshow")
public class SlideShowRestController {

	@PostMapping("/webhook")
	public void webhook(@RequestBody SlideShowRequestDTO.CloudinaryNotification request) {
		log.info(request.toString());
	}
}
