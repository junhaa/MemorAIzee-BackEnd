package memoraize.domain.slideshow.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.exception.AlbumNotExistException;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.slideshow.entity.Memories;
import memoraize.domain.slideshow.repository.MemoriesRepository;
import memoraize.domain.slideshow.web.dto.SlideShowResponseDTO;
import memoraize.global.cloudinary.CloudinaryService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SlideShowQueryServiceImpl implements SlideShowQueryService {
	private final AlbumPostRepository albumPostRepository;
	private final CloudinaryService cloudinaryService;
	private final MemoriesRepository memoriesRepository;

	@Override
	@Transactional
	public String getMemoriesUrl(Long albumId) {
		Album album = albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		Memories memories = album.getMemories();
		if (memories.getUrl() == null) {
			String memoriesUrl = cloudinaryService.getMemories(memories.getSlideShowChunkList());
			memories.setUrl(memoriesUrl);
			memoriesRepository.save(memories);
			return memoriesUrl;
		}
		return memories.getUrl();
	}

	@Override
	public List<SlideShowResponseDTO.SlideShowPreviewResponseDto> getSlideShowPreview() {
		List<SlideShowResponseDTO.SlideShowPreviewResponseDto> result = new ArrayList<>();
		for (Memories memories : memoriesRepository.findAll()) {
			if (memories.getUrl() != null) {
				SlideShowResponseDTO.SlideShowPreviewResponseDto dto = SlideShowResponseDTO.SlideShowPreviewResponseDto.builder()
					.slideShowUrl(memories.getUrl())
					.albumName(memories.getAlbum().getAlbumName())
					.mainImageUrl(memories.getAlbum().getPhotoImages().get(0).getImageUrl())
					.userName(memories.getAlbum().getUser().getUserName())
					.build();

				result.add(dto);
				if (result.size() == 2)
					break;
			}
		}
		return result;
	}
}
