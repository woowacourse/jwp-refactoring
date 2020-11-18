package kitchenpos.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = OrderStatusValidator.class)
@Target({METHOD, CONSTRUCTOR})
@Retention(RUNTIME)
@Documented
public @interface OrderStatusValidate {
    String message() default
        "Order of table is not completion yet. First, complete order.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
