package memoraize.domain.photo.exception;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class MetadataNotExistException extends GeneralException {
	public MetadataNotExistException() {
		super(ErrorStatus._METADATA_NOT_EXIST);
	}
}
