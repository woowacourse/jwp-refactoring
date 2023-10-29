package kitchenpos.core.ordertable.application;

import kitchenpos.core.ordertable.domain.OrderTable;

public interface OrderTablesChangingEmptinessValidator {
    void validate(OrderTable orderTable);
}
