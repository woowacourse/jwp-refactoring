package kitchenpos.ordertable.application.validator;

import kitchenpos.ordertable.OrderTable;

public interface OrderStatusValidator {

    void validateCompletion(final OrderTable orderTable);
}
