package kitchenpos.ordertable.domain.repository;

import kitchenpos.ordertable.domain.OrderTable;

public interface OrdersValidator {

    void validateOrderStatusInOrderTable(OrderTable orderTable);

}
