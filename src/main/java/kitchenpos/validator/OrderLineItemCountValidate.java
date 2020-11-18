package kitchenpos.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = OrderLineItemCountValidator.class)
@Target({METHOD, FIELD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface OrderLineItemCountValidate {
    String message() default "Order line items are not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
