package memoraize.global.enums.statuscode;

import org.springframework.http.HttpStatus;

public enum ErrorStatus implements BaseCode {
	// common
	_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
	_BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
	_FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

	_PAGE_VARIABLE_INVALID(HttpStatus.BAD_REQUEST, "PAGE4001", "요청한 페이지 혹은 페이지당 요소 개수는 0보다 커야합니다."),

	// User
	_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "존재하지 않는 사용자입니다."),
	_EXIST_LOGINID(HttpStatus.BAD_REQUEST, "USER4002", "이미 존재하는 로그인 ID 입니다."),

	_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "USER4003", "이미 존재하는 사용자 이름 입니다."),

	// Album
	_PHOTO_IMAGE_NOT_EXIST(HttpStatus.BAD_REQUEST, "ALBUM4001", "업로드된 사진이 없습니다."),
	_PAGE_NOT_NEGATIVE(HttpStatus.BAD_REQUEST, "ALBUM4002", "page번호나 pageCount는 음수일 수 없습니다."),
	_INVALID_SORT_STATUS(HttpStatus.BAD_REQUEST, "ALBUM4003", "정렬 기준이 올바르지 않습니다."),
	_ALBUM_FORBIDEN(HttpStatus.FORBIDDEN, "ALBUM4004", "앨범에 대한 접근 권한이 없습니다."),
	_ALBUM_NOT_EXIST(HttpStatus.BAD_REQUEST, "ALBUM4005", "존재하지 않는 앨범입니다."),

	//Photo
	_PHOTO_NOT_EXIST(HttpStatus.BAD_REQUEST, "PHOTO4001", "존재하지 않는 사진입니다."),
	_METADATA_NOT_EXIST(HttpStatus.BAD_REQUEST, "PHOTO4002", "메타데이터가 존재하지 않는 사진입니다."),

	// Place
	_PLACE_FETCH_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PLACE5001", "장소에 중복되는 값이 있습니다."),
	_PLACE_NOT_EXIST(HttpStatus.BAD_REQUEST, "PLACE4001", "존재하지 않는 장소입니다."),

	//S3
	_S3_FILE_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S35001", "S3 파일 저장에 실패했습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	ErrorStatus(HttpStatus httpStatus, String code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public Integer getStatusValue() {
		return httpStatus.value();
	}
}
