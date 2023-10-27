package kitchenpos.ordertable.domain.validator;

import kitchenpos.ordertable.domain.OrderTable;

public interface OrderTableValidator {
    void validateCompletedOrderTable(final OrderTable orderTable);
}
