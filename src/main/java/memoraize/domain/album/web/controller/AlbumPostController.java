package memoraize.domain.album.web.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.service.AlbumPostCommandService;
import memoraize.domain.album.web.dto.AlbumPostRequestDTO;
import memoraize.domain.album.web.dto.AlbumPostResponseDTO;
import memoraize.global.response.ApiResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumPostController {
		private final AlbumPostCommandService albumPostCommandService;
	@PostMapping("")
	public ApiResponse<AlbumPostResponseDTO.addAlbumPostResultDTO> addAlbum(@Valid @ModelAttribute AlbumPostRequestDTO.addAlbumPostDTO request){
		AlbumPostResponseDTO.addAlbumPostResultDTO addAlbumPostResultDTO = albumPostCommandService.addAlbum(request);
		return ApiResponse.onSuccess(addAlbumPostResultDTO);
	}
}
