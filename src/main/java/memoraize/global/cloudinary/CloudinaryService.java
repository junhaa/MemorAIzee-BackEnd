package memoraize.global.cloudinary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.entity.PhotoHashTag;
import memoraize.domain.photo.enums.TagCategory;
import memoraize.domain.photo.exception.PhotoNotExistException;
import memoraize.domain.photo.repository.PhotoRepository;
import memoraize.domain.slideshow.entity.SlideShowChunk;
import memoraize.global.aws.s3.AmazonS3Manager;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;
import memoraize.global.util.BasicAuthHeader;
import memoraize.global.util.FFmpegService;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

	@Value("${cloud.cloudinary.api-url}")
	private String apiUrl;

	@Value("${cloud.cloudinary.upload-preset-id}")
	private String uploadPreset;

	@Value("${cloud.cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloud.cloudinary.api-key}")
	private String apiKey;

	@Value("${cloud.cloudinary.api-secret}")
	private String apiSecret;

	private static final Logger log = LogManager.getLogger(CloudinaryService.class);

	private final FFmpegService ffmpegService;

	private final ObjectMapper objectMapper;
	private final PhotoRepository photoRepository;
	private final AmazonS3Manager amazonS3Manager;

	public String getMemories(List<SlideShowChunk> slideShowChunkList) {
		List<String> chunkUrlList = new ArrayList<>();
		for (SlideShowChunk slideShowChunk : slideShowChunkList) {
			chunkUrlList.add(getChunkUrl(slideShowChunk.getPublicId()));
		}

		String fileName = ffmpegService.mergeVideoFiles(chunkUrlList.toArray(new String[0]));

		String uuid = UUID.randomUUID().toString();
		String uploadedUrl = amazonS3Manager.uploadFile(amazonS3Manager.generateMemoriesKeyName(uuid), fileName);
		return uploadedUrl;
	}

	public String getChunkUrl(String publicId) {
		String apiUrl = "https://api.cloudinary.com/v1_1/" + cloudName + "/resources/video/upload/" + publicId;
		String token = BasicAuthHeader.generateBasicAuthHeader(apiKey, apiSecret);

		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(apiUrl);

			httpGet.setHeader("Authorization", token);

			HttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				log.error("슬라이드 청크 API 요청에서 에러가 발생했습니다. errorcode = {}", statusCode);
				throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line;
			StringBuilder responseContent = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
			}
			reader.close();

			JsonNode jsonNode = objectMapper.readTree(responseContent.toString());
			String url = jsonNode.get("url").asText();
			log.info("chunk url = {}", url);
			return url;
		} catch (IOException e) {
			log.error("슬라이드 청크 API 요청에서 에러가 발생했습니다. {}", e);
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	public String createVideo(Long photoId) {
		String publicId = UUID.randomUUID().toString();

		Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new PhotoNotExistException());

		List<PhotoHashTag> hashTagList = photo.getPhotoHashTagList();

		String colorCode = null;

		for (PhotoHashTag hashTag : hashTagList) {
			if (hashTag.getTagCategorie() == TagCategory.COLOR) {
				colorCode = hashTag.getTagName();
				break;
			}
		}

		// 만약 색상 코드가 없다면 흰색으로 설정
		if (colorCode == null) {
			colorCode = "#FFFFFF";
		}

		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(apiUrl);

			HttpEntity entity = MultipartEntityBuilder.create()
				.addTextBody("public_id", publicId)
				.addTextBody("resource_type", "video")
				.addTextBody("upload_preset", uploadPreset)
				.addTextBody("manifest_json", generateManifestJson(photo.getImageUrl(), colorCode, photo.getTitle(),
					getHashTagString(hashTagList)))
				.build();

			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				log.error("슬라이드 쇼 제작 API 요청에서 에러가 발생했습니다. errorcode = {}", statusCode);
				throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
			}

			return publicId;
		} catch (IOException e) {
			log.error("슬라이드 쇼 제작 API 요청에서 에러가 발생했습니다. {}", e);
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}

	public String getHashTagString(List<PhotoHashTag> hashTagList) {
		String hashTagString = "";
		for (PhotoHashTag hashTag : hashTagList) {
			if (hashTag.getTagCategorie() == TagCategory.LABEL) {
				hashTagString += "#" + hashTag.getTagName() + " ";
			}
		}
		return hashTagString;
	}

	private String generateManifestJson(String imageUrl, String colorCode, String photoTitle, String hashTagString) {
		String manifest = "{\n"
			+ "  \"type\": \"video\",\n"
			+ "  \"width\": 1280,\n"
			+ "  \"height\": 720,\n"
			+ "  \"duration\": 4,\n"
			+ "  \"fps\": 30,\n"
			+ "  \"vars\": {\n"
			+ "    \"bgColor\": \"" + colorCode + "\",\n"
			+ "    \"imageUrl\": \"" + imageUrl + "\",\n"
			+ "    \"sponsoredText\": \"" + photoTitle + "\",\n"
			+ "    \"titleText\": \"" + hashTagString + "\",\n"
			+ "    \"ctaText\": \"Read more\"\n"
			+ "  },\n"
			+ "  \"tracks\": [\n"
			+ "    {\n"
			+ "      \"clips\": [\n"
			+ "        {\n"
			+ "          \"color\": \"{{bgColor}}\",\n"
			+ "          \"type\": \"color\"\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    },\n"
			+ "    {\n"
			+ "      \"width\": 920,\n"
			+ "      \"height\": 720,\n"
			+ "      \"x\": 0,\n"
			+ "      \"y\": 0,\n"
			+ "      \"clipDefaults\": {\n"
			+ "        \"clipDuration\": 4000,\n"
			+ "        \"clipEffect\": {\n"
			+ "          \"name\": \"KenBurns\",\n"
			+ "          \"easing\": \"easeinout\",\n"
			+ "          \"zoom\": 1.1,\n"
			+ "          \"center\": [\n"
			+ "            0.5,\n"
			+ "            0.5\n"
			+ "          ],\n"
			+ "          \"direction\": \"backward\"\n"
			+ "        }\n"
			+ "      },\n"
			+ "      \"clips\": [\n"
			+ "        {\n"
			+ "          \"media\": [\n"
			+ "            \"{{imageUrl}}\",\n"
			+ "            \"image\",\n"
			+ "            \"fetch\"\n"
			+ "          ],\n"
			+ "          \"type\": \"image\",\n"
			+ "          \"transformation\": \"c_fill\"\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    }, // track 2\n"
			+ "    {\n"
			+ "      \"x\": 950,\n"
			+ "      \"keyframes\": {\n"
			+ "        \"0\": {\n"
			+ "          \"y\": 290,\n"
			+ "          \"opacity\": 0\n"
			+ "        },\n"
			+ "        \"1000\": {\n"
			+ "          \"y\": 290,\n"
			+ "          \"opacity\": 0\n"
			+ "        },\n"
			+ "        \"2000\": {\n"
			+ "          \"y\": 280,\n"
			+ "          \"opacity\": 1\n"
			+ "        }\n"
			+ "      },\n"
			+ "      \"clipDefaults\": {\n"
			+ "        \"fontSize\": 24,\n"
			+ "        \"fontColor\": \"white\"\n"
			+ "      },\n"
			+ "      \"clips\": [\n"
			+ "        {\n"
			+ "          \"text\": \"{{sponsoredText}}\",\n"
			+ "          \"type\": \"text\"\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    }, // track 3\n"
			+ "    {\n"
			+ "      \"x\": 950,\n"
			+ "      \"width\": 300,\n"
			+ "      \"height\": 300,\n"
			+ "      \"keyframes\": {\n"
			+ "        \"0\": {\n"
			+ "          \"y\": 290\n"
			+ "        },\n"
			+ "        \"1000\": {\n"
			+ "          \"y\": 300\n"
			+ "        }\n"
			+ "      },\n"
			+ "      \"clipDefaults\": {\n"
			+ "        \"textAlign\": \"left\",\n"
			+ "        \"fontSize\": 32,\n"
			+ "        \"fontType\": \"Noto Sans\",\n"
			+ "        \"fontColor\": \"white\"\n"
			+ "      },\n"
			+ "      \"clips\": [\n"
			+ "        {\n"
			+ "          \"text\": \"{{titleText}}\",\n"
			+ "          \"type\": \"textArea\"\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    } // track 4\n"
			+ "  ]\n"
			+ "}";

		return manifest;
	}
}
