package memoraize.domain.review.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import memoraize.domain.review.validation.annotation.Pageable;
import memoraize.global.enums.statuscode.ErrorStatus;

public class PageableConstraint implements ConstraintValidator<Pageable, Long> {
	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		boolean isValid = value > 0;
		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ErrorStatus._PAGE_VARIABLE_INVALID.getMessage())
				.addConstraintViolation();
		}
		return isValid;
	}

	@Override
	public void initialize(Pageable constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}
