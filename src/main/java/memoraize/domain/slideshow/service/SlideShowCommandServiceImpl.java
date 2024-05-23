package memoraize.domain.slideshow.service;

import java.util.ArrayList;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.entity.Album;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.slideshow.entity.Memories;
import memoraize.domain.slideshow.entity.SlideShowChunk;
import memoraize.domain.slideshow.repository.SlideShowChunkRepository;
import memoraize.global.cloudinary.CloudinaryService;

@Service
@RequiredArgsConstructor
@Transactional
public class SlideShowCommandServiceImpl implements SlideShowCommandService {

	private final SlideShowChunkRepository slideShowChunkRepository;
	private final CloudinaryService cloudinaryService;

	@Async
	@Override
	public void makeSlideShow(Album album) {
		Memories memories = Memories.builder()
			.slideShowChunkList(new ArrayList<>())
			.build();

		album.setMemories(memories);

		for (Photo photoImage : album.getPhotoImages()) {
			String publicId = cloudinaryService.createVideo(photoImage.getId());
			SlideShowChunk slideShowChunk = SlideShowChunk.builder()
				.publicId(publicId)
				.build();
			memories.addSlideShowChunk(slideShowChunk);
			slideShowChunkRepository.save(slideShowChunk);
		}
	}
}
