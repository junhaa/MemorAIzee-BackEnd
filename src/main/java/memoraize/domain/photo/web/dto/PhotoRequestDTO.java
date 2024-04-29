package memoraize.domain.photo.web.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

/**
 * 이후 앨범 DTO에서 사진을 LIST로 받아오면 제거
 */
public class PhotoRequestDTO {

	@Getter
	@Setter
	public static class savePhotoDTO{
		private List<MultipartFile> photoImages;
	}
}
