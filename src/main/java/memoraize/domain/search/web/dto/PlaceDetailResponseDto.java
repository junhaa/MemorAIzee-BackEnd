package memoraize.domain.search.web.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// place information and map marker
public class PlaceDetailResponseDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PlaceDetail {
		private String place_id;
		private String placeName;
		private String placePhotoUrl;
		private PlaceInfo placeInfo;
		private String address;
		private String phoneNumber;
		private PlaceIcon placeIconInfo;
		private List<String> businessStatus;
		private String placeUrl;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PlaceIcon {
		private String icon;
		private String bgColor;
		private String iconMaskBaseUrl;
	}
	// these are optional fields. for css
	// api docs : https://developers.google.com/maps/documentation/places/web-service/details?hl=ko

	// this is for map marker
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PlaceInfo {
		private double lat;
		private double lng;
	}
}
