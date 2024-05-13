package memoraize.global.util;

import java.util.UUID;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

@Service
@Slf4j
public class FFmpegService {
	public String mergeVideoFiles(String[] inputFiles) {
		String fileName = UUID.randomUUID().toString();
		String outputFile = fileName + ".mp4";  // 출력 파일

		// 출력 비디오 설정: 출력 파일 경로, 너비, 높이
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputFile, 1280, 720);

		// 영상과 오디오 코덱 설정
		recorder.setVideoCodecName("libx264");
		recorder.setAudioCodecName("aac");

		try {
			// 레코더 시작
			recorder.start();

			for (String inputFile : inputFiles) {
				FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputFile);
				grabber.start();

				Frame frame;
				while ((frame = grabber.grab()) != null) {
					recorder.record(frame);  // 프레임을 출력 파일에 기록
				}

				grabber.stop();
				grabber.release();
			}

			recorder.stop();
			recorder.release();
			return outputFile;
		} catch (Exception e) {
			log.error("영상을 합치는 도중 에러가 발생했습니다. {}", e);
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}
}
