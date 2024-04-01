package memoraize.domain.album.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import memoraize.domain.album.validation.validator.ListNotBlankValidator;

@Documented
@Constraint(validatedBy = ListNotBlankValidator.class)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListNotBlank {
	String message() default("값이 비어있을 수 없습니다.");

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
