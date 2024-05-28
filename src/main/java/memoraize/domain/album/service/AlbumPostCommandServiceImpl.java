package memoraize.domain.album.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import memoraize.domain.album.converter.AlbumPostConverter;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.entity.mapping.AlbumLiked;
import memoraize.domain.album.exception.AlbumNotExistException;
import memoraize.domain.album.repository.AlbumLikedRepository;
import memoraize.domain.album.repository.AlbumPostRepository;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.photo.entity.Photo;
import memoraize.domain.photo.service.PhotoCommandService;
import memoraize.domain.review.entity.Place;
import memoraize.domain.review.repository.PlaceRepository;
import memoraize.domain.slideshow.service.SlideShowCommandService;
import memoraize.domain.user.entity.User;
import memoraize.domain.voice.service.VoiceCommandService;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumPostCommandServiceImpl implements AlbumPostCommandService {
	private static final Logger log = LogManager.getLogger(AlbumPostCommandServiceImpl.class);

	private final AlbumPostRepository albumPostRepository;
	private final PlaceRepository placeRepository;
	private final AlbumLikedRepository albumLikedRepository;
	private final PhotoCommandService photoCommandService;
	private final SlideShowCommandService slideShowCommandService;

	private final VoiceCommandService voiceCommandService;

	@Override
	@Transactional
	public AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbum(AlbumPostRequestDTO.addAlbumPostDTO request, User user) {

		ForkJoinPool commonPool = ForkJoinPool.commonPool();
		int parallelism = commonPool.getParallelism();

		log.info("Thread Pool Size = {}", parallelism);
		Album albumPost = AlbumPostConverter.toAlbumPost(request);
		user.addAlbum(albumPost);

		List<CompletableFuture<Photo>> createPhotoFutures = new ArrayList<>();

		for (MultipartFile image : request.getImages()) {
			CompletableFuture<Photo> future = CompletableFuture.supplyAsync(
				() -> photoCommandService.savePhotoImages(image));
			createPhotoFutures.add(future);
		}

		// 모든 비동기 작업 완료 후 결과 처리
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
			createPhotoFutures.toArray(new CompletableFuture[createPhotoFutures.size()]));

		// 모든 작업 완료 후 결과를 처리하기 위해 thenApply를 사용
		CompletableFuture<List<Photo>> allResultsFuture = allFutures.thenApply(v -> {

			List<Photo> results = new ArrayList<>();
			for (CompletableFuture<Photo> future : createPhotoFutures) {
				try {
					results.add(future.get());
				} catch (InterruptedException | ExecutionException e) {
					log.error("photo 데이터를 합치는 도중 에러가 발생했습니다. 1 {}", e.getMessage());
					throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
				}
			}
			return results;
		});

		for (Photo photo : allResultsFuture.join()) {
			if (photo.getPlace() != null) {
				Place place = photo.getPlace();

				Optional<Place> placeOptional = placeRepository.findByPlaceName(place.getPlaceName());
				if (placeOptional.isPresent()) {
					place = placeOptional.get();
					place.addPhoto(photo);
				} else {
					Place save = placeRepository.save(place);
					save.addPhoto(photo);

				}
			}

			albumPost.addPhoto(photo);
		}

		albumPost = albumPostRepository.saveAndFlush(albumPost);

		// 만약 유저가 저장한 음성 녹음이 있으면 내래이션 생성
		if (user.getVoice() != null) {
			for (Photo photo : albumPost.getPhotoImages()) {
				// Async
				voiceCommandService.createPhotoNarrationAndSave(photo, user.getVoice().getVoiceKey());
			}
		}

		slideShowCommandService.makeSlideShow(albumPost);

		return AlbumPostConverter.toAddAlbumPostResultDTO(albumPost);
	}

	@Override
	@Transactional
	public void deleteAlbum(User user, Long albumId) {
		Album album = albumPostRepository.findById(albumId)
			.orElseThrow(() -> new GeneralException(ErrorStatus._ALBUM_NOT_EXIST));

		if (album.getUser().getId() != user.getId()) {
			throw new GeneralException(ErrorStatus._ALBUM_FORBIDEN);
		}

		albumPostRepository.deleteById(albumId);
	}

	@Override
	@Transactional
	public void likeAlbum(User user, Long albumId) {
		Album album = albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		if (albumLikedRepository.findByUserIdAndAlbumId(user.getId(), albumId).isPresent())
			return;
		AlbumLiked albumLiked = AlbumLiked.builder()
			.album(album)
			.user(user)
			.build();

		albumLikedRepository.save(albumLiked);
	}

	@Override
	@Transactional
	public void unlikeAlbum(User user, Long albumId) {
		albumPostRepository.findById(albumId).orElseThrow(() -> new AlbumNotExistException());
		albumLikedRepository.findByUserIdAndAlbumId(user.getId(), albumId).ifPresent(albumLiked -> {
			albumLikedRepository.delete(albumLiked);
		});
	}

}
