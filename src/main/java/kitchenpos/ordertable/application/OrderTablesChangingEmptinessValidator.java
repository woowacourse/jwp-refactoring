package kitchenpos.ordertable.application;

import kitchenpos.ordertable.domain.OrderTable;

public interface OrderTablesChangingEmptinessValidator {
    void validate(OrderTable orderTable);
}
