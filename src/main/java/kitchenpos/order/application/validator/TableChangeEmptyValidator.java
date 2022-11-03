package kitchenpos.order.application.validator;

import kitchenpos.order.domain.OrderTable;

public interface TableChangeEmptyValidator {

    void validate(final OrderTable orderTable);
}
