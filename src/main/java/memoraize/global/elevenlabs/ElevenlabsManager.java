package memoraize.global.elevenlabs;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.andrewcpu.elevenlabs.ElevenLabs;
import net.andrewcpu.elevenlabs.builders.SpeechGenerationBuilder;
import net.andrewcpu.elevenlabs.enums.GeneratedAudioOutputFormat;
import net.andrewcpu.elevenlabs.enums.StreamLatencyOptimization;
import net.andrewcpu.elevenlabs.model.voice.Voice;
import net.andrewcpu.elevenlabs.model.voice.VoiceBuilder;
import net.andrewcpu.elevenlabs.model.voice.VoiceSettings;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

/**
 * https://github.com/Andrewcpu/elevenlabs-api
 */

@Component
@RequiredArgsConstructor
public class ElevenlabsManager {

	private static final Logger log = LogManager.getLogger(ElevenlabsManager.class);

	@Value("${voice.elevenlabs.apikey}")
	private String apiKey;

	@Value("${voice.elevenlabs.narration-voice-setting.similarity_boost}")
	private double Nsimilarity;
	@Value("${voice.elevenlabs.narration-voice-setting.stability}")
	private double Nstability;
	@Value("${voice.elevenlabs.narration-voice-setting.style}")
	private double Nstyle;
	@Value("${voice.elevenlabs.narration-voice-setting.use-speaker-boost}")
	private boolean NuseSpeakerBoost;

	@PostConstruct
	public void init() {
		ElevenLabs.setApiKey(apiKey);
		ElevenLabs.setDefaultModel("eleven_multilingual_v2");
	}

	/**
	 * EleventLabs TTS 변환
	 * @param text 음성 TTS 변환 할 내용
	 * @param voiceId voice 고유 값
	 * @return 생성된 .mpga 파일
	 */
	public File elevenLabsTTS(String text, String voiceId) {
		return SpeechGenerationBuilder.textToSpeech()
			.file()
			.setText(text)
			.setGeneratedAudioOutputFormat(GeneratedAudioOutputFormat.MP3_44100_128)
			.setVoiceId(voiceId)
			.setVoiceSettings(new VoiceSettings(Nstability, Nsimilarity, Nstyle, NuseSpeakerBoost))
			.setLatencyOptimization(StreamLatencyOptimization.NONE)
			.build();
	}

	/**
	 * Voice 생성
	 * @param userName 유저 이름 (voice에 저장될 이름)
	 * @param sample 실제 음성 녹음 파일
	 * @return voice 고유번호
	 */
	public String addVoice(String userName, MultipartFile sample) {
		File file = convertMultipartFileToFile(sample);
		VoiceBuilder builder = new VoiceBuilder();
		builder.withName(userName);
		builder.withFile(file);
		builder.withDescription("Voice describing the travel picture.");
		builder.withLabel("language", "ko");
		Voice voice = builder.create();
		if (file != null && file.exists()) {
			file.delete();
		}
		return voice.getVoiceId();
	}

	/**
	 * ElevenLabs에 저장된 음성 삭제
	 */
	public void deleteVoice(String voiceId) {
		Voice voice = Voice.getVoice(voiceId);
		voice.delete();
	}

	/**
	 * MultiPartFile -> File 변환
	 * @param multipartFile
	 * @return
	 */
	public static File convertMultipartFileToFile(MultipartFile multipartFile) {
		// 임시 파일 생성
		File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());

		// MultipartFile의 내용을 파일에 쓰기
		try {
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeneralException(ErrorStatus._FILE_CONVERT_ERROR);
		}

		return file;
	}
}
