package kitchenpos.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = OrderTableEmptyValidator.class)
@Target({ METHOD, FIELD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface OrderTableEmptyValidate {
    String message() default "Order table is must be empty.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
