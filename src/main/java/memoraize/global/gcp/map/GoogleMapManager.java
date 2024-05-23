package memoraize.global.gcp.map;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.global.aws.s3.AmazonS3Manager;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;
import memoraize.global.gcp.map.dto.GooglePlaceApiResponseDTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMapManager {

	@Value("${cloud.google.map.api-key}")
	private String apiKey;

	private final AmazonS3Manager amazonS3Manager;

	public Optional<String> reverseGeocodingWithGoogleMap(double latitude, double longitude) throws
		IOException,
		InterruptedException,
		ApiException {
		GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(apiKey)
			.build();

		LatLng coordinates = new LatLng(latitude, longitude);
		GeocodingResult[] results = GeocodingApi.reverseGeocode(context, coordinates)
			.language("ko")
			.await();

		if (results.length > 0) {
			return Optional.ofNullable(results[0].formattedAddress);
		} else
			return Optional.ofNullable(null);
	}

	public Optional<GooglePlaceApiResponseDTO> placeSearchWithGoogleMap(double longitude, double latitude) {
		WebClient webClient = WebClient.create();

		String requestBody = "{\n"
			+ "  \"maxResultCount\": 2,\n"
			+ "  \"locationRestriction\": {\n"
			+ "    \"circle\": {\n"
			+ "      \"center\": {\n"
			+ "        \"latitude\": " + latitude + ",\n"
			+ "        \"longitude\":" + longitude + "},\n"
			+ "      \"radius\": 200.0\n"
			+ "    }\n"
			+ "  },\n"
			+ "  \"languageCode\": \"ko\"\n"
			+ "}";

		GooglePlaceApiResponseDTO result = webClient.post()
			.uri("https://places.googleapis.com/v1/places:searchNearby")
			.header("Content-Type", "application/json")
			.header("X-Goog-Api-Key", apiKey)
			.header("X-Goog-FieldMask", "places.displayName,places.id,places.photos")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(GooglePlaceApiResponseDTO.class)
			.block();

		if (result.getPlaces() == null) {
			return Optional.ofNullable(null);
		}

		return Optional.ofNullable(result);
	}

	public String getPlacePhotoWithGoogleMap(String referenceName) {
		byte[] imageBytes = requestPlacePhoto(referenceName);
		return amazonS3Manager.uploadFile(amazonS3Manager.generatePlacePhotoImageKeyName(), imageBytes, "image/jpeg");
	}

	private byte[] requestPlacePhoto(String referenceName) {
		HttpClient client = HttpClient.newBuilder()
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.build();

		try {
			HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(
					"https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&photoreference=" + referenceName
						+ "&key=" + apiKey))
				.GET()
				.build();
			return client.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
		} catch (Exception e) {
			log.error("구글 장소 사진 요청 중 오류가 발생했습니다. {}", e);
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}

}
