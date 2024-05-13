package memoraize.global.util;

import java.io.File;
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
		recorder.setVideoBitrate(10000000);  // 예: 비트레이트를 10 Mbps로 설정

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

			File myfile = new File(outputFile);
			while (!myfile.exists()) {
				try {
					Thread.sleep(500); // 파일이 생성될 때까지 0.5초마다 확인
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
					throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
				}
			}

			return outputFile;
		} catch (Exception e) {
			log.error("영상을 합치는 도중 에러가 발생했습니다. {}", e);
			throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}

	public void deleteFile(String filePath) {
		File file = new File(filePath);

		// 파일이 존재하는지 확인
		if (file.exists()) {
			file.delete();
		}
	}
}
