package kitchenpos.validator;

import java.util.List;

public interface OrderValidator {

    void validateById(final Long id);

    void validateByIdIn(final List<Long> ids);
}
