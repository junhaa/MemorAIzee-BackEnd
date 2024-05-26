package memoraize.domain.review.converter;

import java.util.ArrayList;

import memoraize.domain.review.entity.Place;

public class PlaceConverter {
	public static Place toPlace(String placeName, String googlePlaceId, String photoUrl) {
		return Place.builder()
			.placeName(placeName)
			.googleMapId(googlePlaceId)
			.photoUrl(photoUrl)
			.photoList(new ArrayList<>())
			.reviewList(new ArrayList<>())
			.build();
	}
}
