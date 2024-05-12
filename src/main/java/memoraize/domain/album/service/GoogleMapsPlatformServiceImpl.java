package memoraize.domain.album.service;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.GoogleMapsPlatformRequestDTO;
import memoraize.domain.album.web.dto.GoogleMapsPlatformResponseDTO;
import memoraize.domain.photo.entity.Metadata;
import memoraize.domain.photo.entity.Photo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional( readOnly = true)
public class GoogleMapsPlatformServiceImpl implements GoogleMapsPlatformService{

    private final AlbumPostRepository albumPostRepository;

    @Override
    public GoogleMapsPlatformResponseDTO.WayPointsList getWayPoints(GoogleMapsPlatformRequestDTO googleMapsPlatformRequestDTO) {

        Optional<Album> albumOptional = albumPostRepository.findById(googleMapsPlatformRequestDTO.getAlbumId());

        Album album = albumOptional.orElseThrow(() -> new RuntimeException(""));

        List<Photo> photos = album.getPhotoImages();
        Map<String, List<Photo>> dateOfPhotosMap = new HashMap<>();

        for(Photo photo : photos){
            String photoDate = photo.getCreatedAt().toLocalDate().toString();
            if(!dateOfPhotosMap.containsKey(photoDate)){
                dateOfPhotosMap.put(photoDate, new ArrayList<>());
            }
            dateOfPhotosMap.get(photoDate).add(photo);
        }

        List<GoogleMapsPlatformResponseDTO.GetMedataByDate> metadataListByDate = new ArrayList<>();

        // Date에 따라 저장한 Photo 리스트에 대한 for문
        for(String date : dateOfPhotosMap.keySet()){
            List<GoogleMapsPlatformResponseDTO.GetMetadataList> metadataLists = new ArrayList<>();
            for(Photo photoId : dateOfPhotosMap.get(date)){

                GoogleMapsPlatformResponseDTO.GetMetadataList metadata = new GoogleMapsPlatformResponseDTO.GetMetadataList();

                Metadata photoMetadata = photoId.getMetadata();

                metadata.setLat(photoMetadata.getLatiitude());
                metadata.setLng(photoMetadata.getLongitude());
                metadata.setPlaceTitle(photoId.getPlace().getPlaceName());

                metadataLists.add(metadata);

            }
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
