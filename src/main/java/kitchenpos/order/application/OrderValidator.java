package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

public interface OrderValidator {

    void validate(Order order);
}
