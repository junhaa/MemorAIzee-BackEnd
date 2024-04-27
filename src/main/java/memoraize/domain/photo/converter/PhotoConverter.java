package memoraize.domain.photo.converter;

import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;

public class PhotoConverter {
	public static PhotoResponseDTO.saveReviewResultDTO toSaveReviewResultDTO(Photo photo) {
		return PhotoResponseDTO.saveReviewResultDTO.builder()
			.imageId(photo.getId())
			.imageUrl(photo.getImageUrl())
			.createdAt(photo.getCreatedAt())
			.build();
	}

	public static Photo toPhoto(String imageUrl, String comment) {
		return Photo.builder()
			.comment(comment)
			.imageUrl(imageUrl)
			.build();
	}

	public static Photo toPhoto(String imageUrl) {
		return toPhoto(imageUrl, null);
	}

}
