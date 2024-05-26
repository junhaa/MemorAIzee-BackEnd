package memoraize.domain.review.exception;

import memoraize.global.enums.statuscode.BaseCode;
import memoraize.global.exception.GeneralException;

public class PageVariableInvalidException extends GeneralException {
	public PageVariableInvalidException(BaseCode errorStatus) {
		super(errorStatus);
	}
}
