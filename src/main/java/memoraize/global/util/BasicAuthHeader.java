package memoraize.global.util;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthHeader {

	@Value("${cloud.cloudinary.api-key}")
	private String username;

	@Value("${cloud.cloudinary.api-secret}")
	private String password;

	public static String generateBasicAuthHeader(String username, String password) {
		String credentials = username + ":" + password;
		String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
		return "Basic " + encodedCredentials;
	}
}
