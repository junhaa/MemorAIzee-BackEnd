package memoraize.domain.user.exception;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class ExistLoginIdException extends GeneralException {
	public ExistLoginIdException() {
		super(ErrorStatus._EXIST_LOGINID);
	}
}
