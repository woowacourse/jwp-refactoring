package kitchenpos.core.order.application;

import kitchenpos.core.order.domain.Order;

public interface OrderValidator {

    void validate(final Order order);
}
