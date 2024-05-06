package memoraize.domain.user.exception;

import memoraize.global.enums.statuscode.BaseCode;
import memoraize.global.exception.GeneralException;

public class UserNotExistException extends GeneralException {
	public UserNotExistException(BaseCode errorStatus) {
		super(errorStatus);
	}
}
