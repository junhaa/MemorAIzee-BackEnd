package memoraize.domain.photo.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.service.PhotoCommandService;
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

	private final PhotoCommandService photoCommandService;

	@PostMapping("/photo")
	public ApiResponse<List<Photo>> savePhoto(
		@Valid @ModelAttribute List<MultipartFile> request) throws IOException {//http응답으로 들어온 객체를 request에 저장
		log.info("request = {}", request);
		for (MultipartFile image : request) {
			System.out.println("함수 실행했다");
			System.out.println(image.getName());
			System.out.println(image.getSize());
			System.out.println(image.getOriginalFilename());
			//System.out.println(Arrays.toString(image.getBytes()));
		}
		List<Photo> photos = photoCommandService.savePhotoImages(request);//이 부분을 내가 구현한다.
		//결과에 해당하는 DTO도 제작한다
		//request.getPhotoImages()<-rombok 어노테이션이 만들어준 getter
		return ApiResponse.onSuccess(photos);//<-
	}
}
