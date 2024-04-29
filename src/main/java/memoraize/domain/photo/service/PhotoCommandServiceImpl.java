package memoraize.domain.photo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.converter.PhotoConverter;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.entity.PhotoHashTag;
import memoraize.domain.photo.entity.Uuid;
import memoraize.domain.photo.enums.TagCategory;
import memoraize.domain.photo.exception.ExtractPlaceException;
import memoraize.domain.photo.repository.UuidRepository;
import memoraize.domain.review.converter.PlaceConverter;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.exception.PlaceFetchException;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.global.aws.s3.AmazonS3Manager;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.gcp.map.GoogleMapManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoCommandServiceImpl implements PhotoCommandService {

	private final UuidRepository uuidRepository;
	private final AmazonS3Manager amazonS3Manager;
	private final PlaceRepository placeRepository;
	private final GoogleMapManager googleMapManager;
	private final VisionApiService visionApiService;

	/**
	 * 사진 저장
	 * @param request 요청 받은 사진 목록 (MultiPartFile)
	 * @return List<Photo>
	 */

	@Override
	@Transactional
	public List<Photo> savePhotoImages(List<MultipartFile> request) {

		List<Photo> photoList = new ArrayList<>();

		for (MultipartFile image : request) {
			byte[] imageBytes = null;
			try {
				imageBytes = image.getBytes();
			} catch (IOException e) {
				log.error("imageByte 추출 중 오류 발생");
				throw new RuntimeException("MultipartFile byte extract error");
			}

			// 이미지 파일 위치 정보 추출
			GeoLocation geoLocation = extractLocation(image);
			log.info("이미지 위치 메타데이터 = {} // {}", geoLocation.getLatitude(), geoLocation.getLongitude());

			// Google Vision API 호출
			try {
				visionApiService.connect(image, imageBytes);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Map<TagCategory, List<String>> resultMap = visionApiService.getResultMap();
			Set<TagCategory> categorySet = resultMap.keySet();
			List<PhotoHashTag> hashTags = new ArrayList<>();

			//해시태그 리스트 생성
			for (TagCategory category : categorySet) {
				for (String tag : resultMap.get(category)) {
					PhotoHashTag photoHashTag = PhotoHashTag.builder()
						.tagName(tag)
						.tagCategorie(category)
						.genByAI(true)
						.build();

					hashTags.add(photoHashTag);
				}
			}

			// Google map => 위치 호출

			String placeName = null;
			if (geoLocation != null) {
				placeName = googleMapManager.placeSearchWithGoogleMap(geoLocation);
				if (placeName == null) {
					try {
						placeName = googleMapManager.reverseGeocodingWithGoogleMap(geoLocation);
					} catch (Exception e) {
						throw new ExtractPlaceException(ErrorStatus._INTERNAL_SERVER_ERROR);
					}
				}
			}
			// 지역 이름 추출에 성공하면
			Place place = null;
			if (placeName != null) {
				log.info("placeName = {}", placeName);
				Optional<List<Place>> placeList = placeRepository.findByPlaceName(placeName);
				// 유니크한 값이 아니라면
				if (placeList.get().size() > 1) {
					throw new PlaceFetchException(ErrorStatus._PLACE_FETCH_ERROR);
				} else if (placeList.get().size() == 1) {
					place = placeList.get().get(0);
				}
				// 데이터베이스에 존재하지 않는 장소인 경우
				else {
					Place newPlace = PlaceConverter.toPlace(placeName);
					log.info("newPlace.getPlaceName = {}", newPlace.getPlaceName());
					place = placeRepository.save(newPlace);
				}
			}

			log.info("place = {}", place);

			String uuid = UUID.randomUUID().toString();
			Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

			// S3에 이미지 저장
			String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generatePhotoImageKeyName(savedUuid), image,
				imageBytes);
			log.info("S3 Saved Image URL = {}", imageUrl);
			Photo photo = PhotoConverter.toPhoto(imageUrl);

			// TODO : 비동기 처리 시 어떻게 변경할 것인지
			for (PhotoHashTag hashTag : hashTags) {
				photo.addHashTag(hashTag);
			}

			photo.setPlace(place);
			// setPlace, place save
			photoList.add(photo);
		}

		return photoList;
	}

	public static GeoLocation extractLocation(MultipartFile file) {
		try {
			// MultipartFile을 File로 변환. 실제 사용 시 임시 파일을 생성하고 사용 후 삭제해야 할 수 있음.
			File imageFile = convertToFile(file);

			// 사진의 메타데이터를 읽음
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

			// 메타데이터에서 GPS 디렉토리를 가져옴
			GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
			GeoLocation geoLocation = gpsDirectory == null ? null : gpsDirectory.getGeoLocation();

			// 만약 파일이 존재하면 삭제
			if (imageFile != null && imageFile.exists())
				imageFile.delete();

			return geoLocation;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static File convertToFile(MultipartFile multipartFile) throws IOException {
		// 파일 변환 로직 구현 => 운영체제마다 tmp 폴더에 file 생성
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
		multipartFile.transferTo(file);
		return file;
	}
}
