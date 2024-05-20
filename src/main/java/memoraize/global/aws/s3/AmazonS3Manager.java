package memoraize.global.aws.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.photo.entity.Uuid;
import memoraize.global.aws.exception.S3FileSaveException;
import memoraize.global.config.AmazonConfig;
import memoraize.global.enums.statuscode.ErrorStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

	private final AmazonS3 amazonS3;
	private final AmazonConfig amazonConfig;

	// MultipartFile은 한번만 사용 가능
	public String uploadFile(String keyName, MultipartFile file, byte[] fileBytes) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		try {
			PutObjectRequest objectRequest = new PutObjectRequest(amazonConfig.getBucket(), keyName,
				new ByteArrayInputStream(fileBytes), metadata);
			PutObjectResult putObjectResult = amazonS3.putObject(objectRequest);
		} catch (Exception e) {
			log.error("error at AmazonS3Manager uploadFile : {}", (Object)e.getStackTrace());
			throw new S3FileSaveException(ErrorStatus._S3_FILE_SAVE_ERROR);
		}

		return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
	}

	public String uploadFile(String keyName, String localFileUrl) {
		File file = new File(localFileUrl);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("video/mp4");
		metadata.setContentLength(file.length());

		try {
			PutObjectRequest objectRequest = new PutObjectRequest(amazonConfig.getBucket(), keyName,
				new FileInputStream(file), metadata);
			PutObjectResult putObjectResult = amazonS3.putObject(objectRequest);
			file.delete();
		} catch (Exception e) {
			log.error("error at AmazonS3Manager uploadFile : {}", (Object)e.getStackTrace());
			throw new S3FileSaveException(ErrorStatus._S3_FILE_SAVE_ERROR);
		}
		return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
	}

	public String generatePhotoImageKeyName(Uuid uuid) {
		return amazonConfig.getPhotoImagePath() + '/' + uuid.getUuid();
	}

	public String generateReviewImageKeyName(Uuid uuid) {
		return amazonConfig.getReviewImagePath() + '/' + uuid.getUuid();
	}

	public String generateMemoriesKeyName(String uuid) {
		return amazonConfig.getMemoriesPath() + '/' + uuid;
	}

	public String generateProfileImageKeyName() {
		String uuid = UUID.randomUUID().toString();
		return "user/profile/" + uuid;
	}

}
