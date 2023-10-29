package kitchenpos.validator;

import java.util.List;

public interface OrderStatusValidator {

    void validateById(final Long id);

    void validateByIdIn(final List<Long> ids);
}
