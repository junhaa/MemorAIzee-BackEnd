package memoraize.domain.photo.converter;

import java.util.ArrayList;

import memoraize.domain.album.entity.Album;
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

	public static Photo toPhoto(String imageUrl, String comment, Album album) {
		return Photo.builder()
			.comment(comment)
			.imageUrl(imageUrl)
			.album(album)
			.photoHashTagList(new ArrayList<>())
			.build();
	}

	public static Photo toPhoto(String imageUrl, Album album) {
		return toPhoto(imageUrl, null, album);
	}

}
