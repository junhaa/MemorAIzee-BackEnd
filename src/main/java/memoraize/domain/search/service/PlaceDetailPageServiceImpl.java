package memoraize.domain.search.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import memoraize.domain.review.entity.Review;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.review.repository.ReviewRepository;
import memoraize.domain.search.web.dto.PlaceDetailRequestDto;
import memoraize.domain.search.web.dto.PlaceDetailResponseDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceDetailPageServiceImpl implements PlaceDetailPageService{

    @Value("${cloud.google.map.api-key}")
    private String apiKey;

    private final PlaceRepository placeRepository;

    private final ReviewRepository reviewRepository;

    // this is for getting place detail && mapping marker
    @Override
    public PlaceDetailResponseDto.PlaceDetail getPlaceDetail(Long placeId) {
        String place_id = placeRepository.findById(placeId).get().getGoogleMapId();

        // placeName, geometry, address, phoneNumber, placeIconInfo, businessStatus, placeUrl
        String field = "name,geometry,formatted_address,international_phone_number,icon,icon_background_color,icon_mask_base_uri,opening_hours,url";

        String requestUrl = "https://maps.googleapis.com/maps/api/place/details/json?fields=" + field + "&place_id=" + place_id + "&key=" + apiKey + "&language=ko";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        PlaceDetailResponseDto.PlaceDetail placeDetailResponseDto = new PlaceDetailResponseDto.PlaceDetail();
        PlaceDetailResponseDto.PlaceIcon placeIconInfo = new PlaceDetailResponseDto.PlaceIcon();
        PlaceDetailResponseDto.PlaceInfo placeInfo = new PlaceDetailResponseDto.PlaceInfo();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            String responseBody = response.body().string();

            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            String status = jsonObject.get("status").getAsString();

            if (!status.equals("OK")) {
                throw new IOException("Unexpected response code: " + status);
            }

            JsonObject result = jsonObject.getAsJsonObject("result");
            JsonObject geometry = result.getAsJsonObject("geometry");
            JsonObject openingHours = result.getAsJsonObject("opening_hours");
            JsonArray weekdayTextArray = openingHours.getAsJsonArray("weekday_text");

            String name = result.get("name").getAsString();
            double lat = geometry.getAsJsonObject("location").get("lat").getAsDouble();
            double lng = geometry.getAsJsonObject("location").get("lng").getAsDouble();
            String formatted_address = result.get("formatted_address").getAsString();
            String international_phone_number = result.get("international_phone_number").getAsString();
            String icon = result.get("icon").getAsString();
            String icon_background_color = result.get("icon_background_color").getAsString();
            String icon_mask_base_uri = result.get("icon_mask_base_uri").getAsString();
            String url = result.get("url").getAsString();

            List<String> weekdayTextList = new ArrayList<>();
            for (JsonElement element : weekdayTextArray) {
                weekdayTextList.add(element.getAsString());
            }

            placeDetailResponseDto.setPlace_id(place_id);
            placeDetailResponseDto.setPlaceName(name);
            placeDetailResponseDto.setAddress(formatted_address);
            placeDetailResponseDto.setPhoneNumber(international_phone_number);

            placeIconInfo.setIcon(icon);
            placeIconInfo.setBgColor(icon_background_color);
            placeIconInfo.setIconMaskBaseUrl(icon_mask_base_uri);

            placeDetailResponseDto.setPlaceIconInfo(placeIconInfo);
            placeDetailResponseDto.setBusinessStatus(weekdayTextList);
            placeDetailResponseDto.setPlaceUrl(url);

            placeInfo.setLat(lat);
            placeInfo.setLng(lng);
            placeDetailResponseDto.setPlaceInfo(placeInfo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return placeDetailResponseDto;

    }

    // get all review for specific place
    @Override
    public Optional<List<Review>> getAllReviews(Long placeId) {
        Optional<List<Review>> reviews = reviewRepository.findByPlaceId(placeId);
        if (!reviews.isPresent()) {
            // 예외처리
            return Optional.empty();
        }
        return Optional.of(reviews.get());
    }

}
