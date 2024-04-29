package memoraize.domain.photo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.ColorInfo;
import com.google.cloud.vision.v1.DominantColorsAnnotation;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.enums.TagCategory;

@Slf4j
@RequiredArgsConstructor
@Service
@Getter
public class VisionApiServiceImpl implements VisionApiService {
	private Map<TagCategory, List<String>> resultMap = new HashMap<>();
	@Value("${cloud.google.vision-api.number-of-label}")
	private int numberOfLable;

	@Value("${cloud.google.vision-api.number-of-label}")
	private int numberOfProperties;

	@Override
	public Map<TagCategory, List<String>> getResultMap() {
		return resultMap;
	}

	@Override
	public void connect(MultipartFile image, byte[] imageBytes) throws IOException {
		GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
		resultMap.clear();
		resultMap.put(TagCategory.LABEL, detectLabel(image, imageBytes));
		resultMap.put(TagCategory.COLOR, detectColor(image, imageBytes));
	}

	@Override
	public List<String> detectLabel(MultipartFile image, byte[] imageBytes) {
		ByteString imgBytes = null;
		List<AnnotateImageRequest> requests = new ArrayList<>();
		List<String> result = new ArrayList<>();

		imgBytes = ByteString.copyFrom(imageBytes);
		Image img = Image.newBuilder().setContent(imgBytes).build();

		Feature feat = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
		request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {

				if (res.hasError()) {
					System.out.format("Error: %s%n", res.getError().getMessage());
					return null;
				}

				int i = 0;
				for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
					result.add(annotation.getDescription());
					System.out.println(annotation.getDescription());
					if (++i >= numberOfLable)
						break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("이미지 라벨 분석 도중 오류가 발생했습니다.");
		}
		for (String st : result) {
			System.out.println(st);
		}

		return result;
	}

	@Override
	public List<String> detectColor(MultipartFile image, byte[] imageBytes) throws IOException {
		ByteString imgBytes = null;
		List<AnnotateImageRequest> requests = new ArrayList<>();
		List<String> result = new ArrayList<>();

		imgBytes = ByteString.copyFrom(imageBytes);
		Image img = Image.newBuilder().setContent(imgBytes).build();

		Feature feat = Feature.newBuilder().setType(Feature.Type.IMAGE_PROPERTIES).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
		request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					System.out.format("Error: %s%n", res.getError().getMessage());
					return null;
				}
				int i = 0;

				DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
				for (ColorInfo color : colors.getColorsList()) {//리스트를 반환하나보네 public List<ColorInfo> getColorsList()
					int r = (int)color.getColor().getRed();
					int g = (int)color.getColor().getGreen();
					int b = (int)color.getColor().getBlue();

					System.out.format(
						"fraction: %f%nr: %d, g: %d, b: %d%n",
						color.getPixelFraction(),
						r, g, b);

					result.add(rgbToHex(r, g, b));
					if (++i >= numberOfProperties)
						break;
				}

			}
		}
		for (String st : result) {
			System.out.println(st);
		}

		return result;
	}

	public String rgbToHex(int r, int g, int b) {

		String hexR = Integer.toHexString(r);
		String hexG = Integer.toHexString(g);
		String hexB = Integer.toHexString(b);

		// 16진수 값이 한 자리 수인 경우 앞에 0을 붙여 두 자리로 만듦
		hexR = (hexR.length() == 1) ? "0" + hexR : hexR;
		hexG = (hexG.length() == 1) ? "0" + hexG : hexG;
		hexB = (hexB.length() == 1) ? "0" + hexB : hexB;

		// 결과를 #RRGGBB 형식으로 반환
		return "#" + hexR + hexG + hexB;
	}

}
