package memoraize.domain.album.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumPostRestController {
	private final AlbumPostCommandService albumPostCommandService;
	private final AlbumPostQueryService albumPostQueryService;

	@PostMapping("")
	public ApiResponse<AlbumPostResponseDTO.AddAlbumPostResultDTO> addAlbum(
		@Valid @ModelAttribute AlbumPostRequestDTO.addAlbumPostDTO request, @LoginUser User user) {
		AlbumPostResponseDTO.AddAlbumPostResultDTO addAlbumPostResultDTO = albumPostCommandService.addAlbum(request,
			user);
		return ApiResponse.onSuccess(addAlbumPostResultDTO);
	}

	@GetMapping("")
	public ApiResponse<AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO> getAllAlbumPage(
		@RequestParam(name = "sortStatus") SortStatus sortStatus,
		@Pageable @RequestParam(name = "page") Integer page,
		@Pageable @RequestParam(name = "pageCount") Integer pageCount) {
		AlbumPostResponseDTO.AlbumPostPreviewResultPageDTO resultPageDTO = albumPostQueryService.getAlbumPage(
			sortStatus, page, pageCount);
		return ApiResponse.onSuccess(resultPageDTO);
	}

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

}
