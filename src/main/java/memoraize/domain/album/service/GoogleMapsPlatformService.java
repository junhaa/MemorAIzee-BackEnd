package memoraize.domain.album.service;

import memoraize.domain.album.web.dto.GoogleMapsPlatformResponseDTO;

public interface GoogleMapsPlatformService {
    GoogleMapsPlatformResponseDTO.WayPointsList getWayPoints(Long albumId);
}
