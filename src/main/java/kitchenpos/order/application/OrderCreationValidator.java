package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

public interface OrderCreationValidator {
    void validate(final Order order);
}
