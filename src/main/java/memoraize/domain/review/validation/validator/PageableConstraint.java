package memoraize.domain.review.validation.validator;

import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memoraize.domain.review.validation.annotation.Pageable;
import memoraize.global.enums.statuscode.ErrorStatus;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageableConstraint implements ConstraintValidator<Pageable, Integer> {
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null)
			return true;
		boolean isValid = value > 0;
		if (!isValid) {
			log.error("validation 에러 발생");
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(ErrorStatus._PAGE_VARIABLE_INVALID.getMessage().toString())
				.addConstraintViolation();
		}
		return isValid;
	}

	@Override
	public void initialize(Pageable constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}
}
