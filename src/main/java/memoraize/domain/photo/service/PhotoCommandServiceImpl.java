package memoraize.domain.photo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import memoraize.domain.photo.entity.Uuid;
import memoraize.domain.photo.repository.PhotoRepository;
import memoraize.domain.photo.repository.UuidRepository;
import memoraize.domain.photo.web.dto.PhotoRequestDTO;
import memoraize.domain.photo.web.dto.PhotoResponseDTO;
import memoraize.global.aws.s3.AmazonS3Manager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoCommandServiceImpl implements PhotoCommandService {

	private final UuidRepository uuidRepository;
	private final AmazonS3Manager amazonS3Manager;
	private final PhotoRepository photoRepository;

	@Override
	public List<Photo> savePhotoImages(List<MultipartFile> request){

		List<Photo> photoList = new ArrayList<>();

		for(MultipartFile image : request){

			// 이미지 파일 위치 정보 추출
			GeoLocation geoLocation = extractLocation(image);
			log.info("geoLocation = {}", geoLocation);
			// Google Vision API 호출

			// Google map => 위치 호출



			String uuid = UUID.randomUUID().toString();
			Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

			// S3에 이미지 저장
			String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generatePhotoImageKeyName(savedUuid), image);
			log.info("S3 Saved Image URL = {}", imageUrl);

			Photo photo = Photo.builder()
				.imageUrl(imageUrl)
				//해시태그 리스트 추가
				.build();

			// setPlace, place save

			photoList.add(photo);
		}

		return photoList;
	}

	private static GeoLocation extractLocation(MultipartFile file) {
		try {
			// MultipartFile을 File로 변환. 실제 사용 시 임시 파일을 생성하고 사용 후 삭제해야 할 수 있음.
			File imageFile = convertToFile(file);

			// 사진의 메타데이터를 읽음
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

			// 메타데이터에서 GPS 디렉토리를 가져옴
			GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
			GeoLocation geoLocation = gpsDirectory == null ? null : gpsDirectory.getGeoLocation();

			// 만약 파일이 존재하면 삭제
			if(imageFile != null && imageFile.exists())
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
