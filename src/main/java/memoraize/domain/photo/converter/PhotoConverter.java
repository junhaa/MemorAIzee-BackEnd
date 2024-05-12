package memoraize.domain.photo.converter;

import java.util.ArrayList;

import memoraize.domain.album.entity.Album;
import memoraize.domain.photo.entity.Metadata;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;
import memoraize.domain.review.entity.Place;

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

	public static PhotoResponseDTO.PhotoPreviewDTO toPhotoPreviewDTO(Photo photo) {
		return PhotoResponseDTO.PhotoPreviewDTO.builder()
			.photo_id(photo.getId())
			.photo_url(photo.getImageUrl())
			.location(toLocationDTO(photo.getPlace(), photo))
			.build();
	}

	public static PhotoResponseDTO.LocationDTO toLocationDTO(Place place, Photo photo) {
		Metadata metadata = photo.getMetadata();
		return PhotoResponseDTO.LocationDTO.builder()
			.latitude(metadata.getLatiitude())
			.longitude(metadata.getLongitude())
			.place_id(place.getId())
			.placeName(place.getPlaceName())
			.date(metadata.getDate())
			.build();
	}

}
