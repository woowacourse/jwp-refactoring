package kitchenpos.order.application.validator;

import kitchenpos.order.domain.Order;

public interface OrderValidator {

    void validate(final Order order);
}
