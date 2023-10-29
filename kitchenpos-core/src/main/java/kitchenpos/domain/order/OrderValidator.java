package kitchenpos.domain.order;

import kitchenpos.domain.order.Order;

public interface OrderValidator {

    void validate(Order order);
}
