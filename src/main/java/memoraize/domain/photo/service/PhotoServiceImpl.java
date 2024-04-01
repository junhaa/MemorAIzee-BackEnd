package memoraize.domain.photo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
public class PhotoServiceImpl implements PhotoService{

	private final UuidRepository uuidRepository;
	private final AmazonS3Manager amazonS3Manager;
	private final PhotoRepository photoRepository;

	@Override
	@Transactional
	public List<PhotoResponseDTO.saveReviewResultDTO> savePhotoImages(PhotoRequestDTO.savePhotoDTO request){

		List<Photo> photoList = new ArrayList<>();
		List<PhotoResponseDTO.saveReviewResultDTO> photoResultList = new ArrayList<>();

		for(MultipartFile image : request.getPhotoImages()){

			// Google Vision API 호출

			String uuid = UUID.randomUUID().toString();
			Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

			// S3에 이미지 저장
			String imageUrl = amazonS3Manager.uploadFile(amazonS3Manager.generatePhotoImageKeyName(savedUuid), image);
			log.info("S3 Saved Image URL = {}", imageUrl);

			Photo photo = Photo.builder()
				.imageUrl(imageUrl)
				//해시태그 리스트 추가
				.build();

			Photo savedPhoto = photoRepository.save(photo);
			photoList.add(savedPhoto);
		}

		for(Photo p : photoList){
			PhotoResponseDTO.saveReviewResultDTO res = PhotoConverter.toSaveReviewResultDTO(p);
			photoResultList.add(res);
		}

		return photoResultList;
	}
}
