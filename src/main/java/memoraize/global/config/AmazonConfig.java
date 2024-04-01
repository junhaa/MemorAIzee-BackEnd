package memoraize.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Configuration
@Getter
public class AmazonConfig {

	private AWSCredentials awsCredentials;

	@Value("${cloud.aws.s3.credentials.access-key}")
	private String accessKey;

	@Value("${cloud.aws.s3.credentials.secret-key}")
	private String secretKey;

	@Value("${cloud.aws.s3.region.static}")
	private String region;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.path.photoImage}")
	private String photoImagePath;



	@PostConstruct
	public void init(){
		this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	}

	@Bean
	public AmazonS3 amazonS3(){
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
			.build();
	}

	@Bean
	public AWSCredentialsProvider awsCredentialsProvider(){
		return new AWSStaticCredentialsProvider(awsCredentials);
	}

}
