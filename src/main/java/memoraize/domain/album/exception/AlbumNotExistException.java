package memoraize.domain.album.exception;

import memoraize.global.enums.statuscode.ErrorStatus;
import memoraize.global.exception.GeneralException;

public class AlbumNotExistException extends GeneralException {
	public AlbumNotExistException() {
		super(ErrorStatus._ALBUM_NOT_EXIST);
	}
}
