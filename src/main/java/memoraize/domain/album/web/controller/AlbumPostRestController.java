package memoraize.domain.album.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import memoraize.domain.album.converter.AlbumPostConverter;
import memoraize.domain.album.entity.Album;
import memoraize.domain.album.enums.SortStatus;
import memoraize.domain.album.service.AlbumPostCommandService;
import memoraize.domain.album.service.AlbumPostQueryService;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.domain.review.validation.annotation.Pageable;
import memoraize.domain.user.entity.User;
import memoraize.global.annotation.LoginUser;
import memoraize.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumPostRestController {
	private static final Logger log = LogManager.getLogger(AlbumPostRestController.class);
	private final AlbumPostCommandService albumPostCommandService;
	private final AlbumPostQueryService albumPostQueryService;

	/**
	 * 앨범 생성
	 */
	@PostMapping("")
	public ApiResponse<AlbumPostResponseDTO.AddAlbumPostResultDTO> addAlbum(
		@Valid @ModelAttribute AlbumPostRequestDTO.addAlbumPostDTO request, @LoginUser User user) {
		AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbumPostResultDTO = albumPostCommandService.addAlbum(request,
			user);
		return ApiResponse.onSuccess(addAlbumPostResultDTO);
	}

	/**
	 * 모든 앨범 페이지 호출
	 */
	@GetMapping("")
	public ApiResponse<AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO> getAllAlbumPage(
		@RequestParam(name = "sortStatus") SortStatus sortStatus,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO resultPageDTO = albumPostQueryService.getAlbumPage(
			sortStatus, page, pageCount);
		return ApiResponse.onSuccess(resultPageDTO);
	}

	/**
	 * 특정 유저가 작성한 앨범 페이지 호출
	 */
	@GetMapping("/users/{userId}")
	public ApiResponse<AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO> getAlbumPage(
		@RequestParam(name = "sortStatus") SortStatus sortStatus,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount,
		@PathVariable(name = "userId") Long userId) {
		Page<Album> userAlbumPage = albumPostQueryService.getUserAlbumPage(userId, sortStatus, page, pageCount);
		AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO result = AlbumPostConverter.toAlbumPostPreviewResultPageDTO(
			userAlbumPage);
		return ApiResponse.onSuccess(result);
	}

	/**
	 * 내가 작성한 앨범 페이지 호출
	 */
	@GetMapping("/users")
	public ApiResponse<AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO> getAlbumPage(
		@RequestParam(name = "sortStatus") SortStatus sortStatus,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount,
		@LoginUser User user) {
		Page<Album> userAlbumPage = albumPostQueryService.getUserAlbumPage(user.getId(), sortStatus, page, pageCount);
		AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO result = AlbumPostConverter.toAlbumPostPreviewResultPageDTO(
			userAlbumPage);
		return ApiResponse.onSuccess(result);
	}

	/**
	 * 앨범 삭제
	 */
	@DeleteMapping("/{albumId}")
	public ApiResponse<String> deleteAlbum(@PathVariable(name = "albumId") Long albumId, @LoginUser User user) {
		albumPostCommandService.deleteAlbum(user, albumId);
		return ApiResponse.onSuccess("성공적으로 앨범을 삭제했습니다.");
	}

	@PostMapping("/like/{albumId}")
	public ApiResponse<?> likedAlbum(@PathVariable(name = "albumId") Long albumId, @LoginUser User user) {
		albumPostCommandService.likeAlbum(user, albumId);
		return ApiResponse.onSuccess("앨범 좋아요 등록에 성공했습니다.");
	}

	@DeleteMapping("/like/{albumId}")
	public ApiResponse<?> unlikedAlbum(@PathVariable(name = "albumId") Long albumId, @LoginUser User user) {
		albumPostCommandService.unlikeAlbum(user, albumId);
		return ApiResponse.onSuccess("앨범 좋아요 취소에 성공했습니다.");
	}

	/**
	 * 앨범 상세 내용
	 */
	@GetMapping("/{albumId}")
	public ApiResponse<AlbumPostResponseDTO.AlbumDetailResponseDTO> getAlbumDetail(
		@PathVariable(name = "albumId") Long albumId) {
		AlbumPostResponseDTO.AlbumDetailResponseDTO result = albumPostQueryService.getAlbumDetail(albumId);
		return ApiResponse.onSuccess(result);
	}
}
