package kitchenpos.util;

import org.springframework.validation.BindingResult;

public class BindingResultValidator {
    public static void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException();
        }
    }
}
