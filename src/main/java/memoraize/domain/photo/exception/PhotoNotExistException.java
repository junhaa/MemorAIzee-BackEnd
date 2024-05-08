package memoraize.domain.photo.exception;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class PhotoNotExistException extends GeneralException {
	public PhotoNotExistException() {
		super(ErrorStatus._PHOTO_NOT_EXIST);
	}
}
