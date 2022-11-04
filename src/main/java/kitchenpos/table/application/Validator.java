package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public interface Validator {

    void validateOrderStatus(final OrderTable orderTable);
}
