package memoraize.domain.voice.web.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import memoraize.domain.user.entity.User;
import memoraize.domain.voice.VoiceConverter;
import memoraize.domain.voice.entity.Voice;
import memoraize.domain.voice.service.VoiceCommandService;
import memoraize.domain.voice.web.dto.VoiceRequestDto;
import memoraize.domain.voice.web.dto.VoiceResponseDto;
import memoraize.global.annotation.LoginUser;
import memoraize.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voice")
public class VoiceRestController {
	private final VoiceCommandService voiceCommandService;

	@PostMapping("")
	public ApiResponse<VoiceResponseDto.AddVoiceResponseDTO> addVoice(@LoginUser User user, @Valid @ModelAttribute
	VoiceRequestDto.AddVoiceRequestDTO request) {
		Voice voice = voiceCommandService.addVoice(request, user);
		return ApiResponse.onSuccess(VoiceConverter.toAddVoiceResponseDTO(voice));
	}
}
