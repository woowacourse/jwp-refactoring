package kitchenpos.order.service;

import kitchenpos.order.domain.Order;

@FunctionalInterface
public interface OrderValidator {

    void validateExistInOrderTable(final Order order);
}
