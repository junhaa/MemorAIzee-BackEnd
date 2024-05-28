package memoraize.domain.album.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.GoogleMapsPlatformResponseDTO;
import memoraize.domain.photo.entity.Metadata;
import memoraize.domain.photo.entity.Photo;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleMapsPlatformServiceImpl implements GoogleMapsPlatformService {

	private static final Logger log = LogManager.getLogger(GoogleMapsPlatformServiceImpl.class);

	private final AlbumPostRepository albumPostRepository;

	@Override
	public GoogleMapsPlatformResponseDTO.WayPointsList getWayPoints(Long albumId) {

		Optional<Album> albumOptional = albumPostRepository.findById(albumId);

		Album album = albumOptional.orElseThrow(() -> new RuntimeException(""));

		List<Photo> photos = album.getPhotoImages();
		Map<String, List<Photo>> dateOfPhotosMap = new HashMap<>();

		for (Photo photo : photos) {
			String photoDate = photo.getMetadata().getDate().toString().substring(0, 10);
			log.info("photoDate: {}", photoDate);
			if (!dateOfPhotosMap.containsKey(photoDate)) {
				dateOfPhotosMap.put(photoDate, new ArrayList<>());
			}
			dateOfPhotosMap.get(photoDate).add(photo);
		}

		List<GoogleMapsPlatformResponseDTO.GetMedataByDate> metadataListByDate = new ArrayList<>();

		// Date에 따라 저장한 Photo 리스트에 대한 for문
		List<String> dateList = new ArrayList<>(dateOfPhotosMap.keySet());
		Collections.sort(dateList);
		for (String date : dateList) {
			Map<String, List<GoogleMapsPlatformResponseDTO.GetMetadataList>> metadataLists = new HashMap<>();
			List<GoogleMapsPlatformResponseDTO.GetMetadataList> photoMetadataList = new ArrayList<>();
			for (Photo photoId : dateOfPhotosMap.get(date)) {

				GoogleMapsPlatformResponseDTO.GetMetadataList metadata = new GoogleMapsPlatformResponseDTO.GetMetadataList();

				Metadata photoMetadata = photoId.getMetadata();

				metadata.setLat(photoMetadata.getLatitude());
				metadata.setLng(photoMetadata.getLongitude());
				metadata.setPlaceTitle(photoId.getPlace().getPlaceName());

				photoMetadataList.add(metadata);

			}
			metadataLists.put(date, photoMetadataList);
			GoogleMapsPlatformResponseDTO.GetMedataByDate metadataByDate = GoogleMapsPlatformResponseDTO.GetMedataByDate.builder()
				.metadataByDate(metadataLists)
				.build();

			metadataListByDate.add(metadataByDate);
		}

		return GoogleMapsPlatformResponseDTO.WayPointsList.builder()
			.wayPointsList(metadataListByDate)
			.build();
	}

}
