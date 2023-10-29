package kitchenpos.core.order.application;

import kitchenpos.core.order.domain.Order;

public interface OrderCreationValidator {
    void validate(final Order order);
}
