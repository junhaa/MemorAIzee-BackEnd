package memoraize.domain.photo.exception;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class PlaceNotExistException extends GeneralException {
	public PlaceNotExistException() {
		super(ErrorStatus._PLACE_NOT_EXIST);
	}
}
