package kitchenpos.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = OrderTableCountValidator.class)
@Target({METHOD, FIELD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface OrderTableCountValidate {
    String message() default "Table of table group request is not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
