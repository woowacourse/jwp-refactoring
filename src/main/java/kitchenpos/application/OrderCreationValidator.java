package kitchenpos.application;

import kitchenpos.domain.Order;

public interface OrderCreationValidator {
    void validate(final Order order);
}
