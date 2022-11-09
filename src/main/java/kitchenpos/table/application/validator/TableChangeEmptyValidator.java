package kitchenpos.table.application.validator;

import kitchenpos.table.domain.OrderTable;

public interface TableChangeEmptyValidator {

    void validate(final OrderTable orderTable);
}
