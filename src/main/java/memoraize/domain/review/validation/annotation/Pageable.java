package memoraize.domain.review.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import memoraize.domain.review.validation.validator.PageableConstraint;

@Documented
@Constraint(validatedBy = PageableConstraint.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Pageable {
	String message() default ("페이지 관련 값은 0보다 커야합니다.");

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
