package memoraize.global.exception;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import memoraize.global.enums.statuscode.BaseCode;

@RequiredArgsConstructor
public class GeneralException extends RuntimeException{
	private final BaseCode errorStatus;

	public String getErrorCode() {
		return errorStatus.getCode();
	}

	public String getErrorReason() {
		return errorStatus.getMessage();
	}

	public HttpStatus getHttpStatus() {
		return errorStatus.getHttpStatus();
	}
}
