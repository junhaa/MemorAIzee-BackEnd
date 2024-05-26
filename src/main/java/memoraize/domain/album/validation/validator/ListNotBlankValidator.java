package memoraize.domain.album.validation.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.album.validation.annotation.ListNotBlank;
import memoraize.global.enums.statuscode.ErrorStatus;

@Slf4j
@RequiredArgsConstructor
@Component
public class ListNotBlankValidator implements ConstraintValidator<ListNotBlank, List<MultipartFile>> {

	@Override
	public boolean isValid(List<MultipartFile> value, ConstraintValidatorContext context) {
		boolean isValid = !value.isEmpty();
		if(!isValid){
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ErrorStatus._PHOTO_IMAGE_NOT_EXIST.getMessage().toString())
				.addConstraintViolation();
		}
		return isValid;
	}

	@Override
	public void initialize(ListNotBlank constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}


