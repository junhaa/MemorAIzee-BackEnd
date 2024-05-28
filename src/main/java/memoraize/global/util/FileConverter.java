package memoraize.global.util;

import java.io.File;
import java.io.FileInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class FileConverter {
	private static final Logger LOGGER = LogManager.getLogger(FileConverter.class);

	public static MultipartFile toMultipartFile(File file, String fileName) {

		MultipartFile multipartFile = null;
		try {
			FileInputStream input = new FileInputStream(file);
			multipartFile = new MockMultipartFile("file",
				file.getName(),
				"audio/mpeg",
				input);
		} catch (Exception e) {
			LOGGER.error("MultipartFile 변환 중 에러가 발생했습니다. {}", e.getMessage());
			throw new GeneralException(ErrorStatus._FILE_CONVERT_ERROR);
		}
		return multipartFile;
	}
}
