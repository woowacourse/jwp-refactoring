package kitchenpos.ordertable.application.validator;

import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;

public interface OrderStatusValidator {

    void validateCompletion(final OrderTable orderTable);

    void validateAllCompletion(final OrderTables orderTables);
}
