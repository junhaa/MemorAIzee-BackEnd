package memoraize.domain.photo.converter;

import java.util.ArrayList;
import java.util.List;

import memoraize.domain.album.entity.Album;
import memoraize.domain.photo.entity.Metadata;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.entity.PhotoHashTag;
import memoraize.domain.photo.enums.TagCategory;
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

	public static Photo toPhoto(String comment, Album album, String title) {
		return Photo.builder()
			.comment(comment)
			.album(album)
			.title(title)
			.photoHashTagList(new ArrayList<>())
			.build();
	}

	public static Photo toPhoto(Album album) {
		return toPhoto("사진에 대한 내용을 작성해주세요.", album, "사진 제목을 작성해주세요.");
	}

	public static PhotoResponseDTO.PhotoPreviewDTO toPhotoPreviewDTO(Photo photo) {
		String colorCode = null;
		for (PhotoHashTag hashTag : photo.getPhotoHashTagList()) {
			if (hashTag.getTagCategorie() == TagCategory.COLOR) {
				colorCode = hashTag.getTagName();
				break;
			}
		}

		return PhotoResponseDTO.PhotoPreviewDTO.builder()
			.photo_id(photo.getId())
			.photo_url(photo.getImageUrl())
			.color_code(colorCode)
			.location(toLocationDTO(photo.getPlace(), photo))
			.build();
	}

	public static PhotoResponseDTO.LocationDTO toLocationDTO(Place place, Photo photo) {
		if (place == null)
			return null;
		Metadata metadata = photo.getMetadata();
		return PhotoResponseDTO.LocationDTO.builder()
			.latitude(metadata.getLatitude())
			.longitude(metadata.getLongitude())
			.place_id(place.getId())
			.place_name(place.getPlaceName())
			.date(metadata.getDate())
			.build();
	}

	public static PhotoResponseDTO.PhotoDetailDTO toPhotoDetailDTO(Photo photo, List<PhotoHashTag> hashTagList) {
		String colorCode = null;
		List<PhotoResponseDTO.HashTagResponseDTO> tagResponseDTOList = new ArrayList<>();
		for (PhotoHashTag hashTag : hashTagList) {
			if (hashTag.getTagCategorie() == TagCategory.COLOR) {
				colorCode = hashTag.getTagName();
			} else {
				tagResponseDTOList.add(toHashTageResponseDTO(hashTag));
			}
		}

		return PhotoResponseDTO.PhotoDetailDTO.builder()
			.photo_id(photo.getId())
			.photo_title(photo.getTitle())
			.photo_comment(photo.getComment())
			.photo_url(photo.getImageUrl())
			.location(toLocationDTO(photo.getPlace(), photo))
			.hashTage_list(tagResponseDTOList)
			.photo_color_code(colorCode)
			.narration_url(photo.getPhotoNarration() == null ? null : photo.getPhotoNarration().getNarrationUrl())
			.build();

	}

	public static PhotoResponseDTO.HashTagResponseDTO toHashTageResponseDTO(PhotoHashTag photoHashTag) {
		return PhotoResponseDTO.HashTagResponseDTO.builder()
			.hashtag_id(photoHashTag.getId())
			.tag_name(photoHashTag.getTagName())
			.generated_by_ai(photoHashTag.isGenByAI())
			.build();
	}

}
