package kitchenpos.table.domain.service;

import kitchenpos.table.domain.model.OrderTable;

public interface TableChangeEmptyValidator {

    void validate(OrderTable orderTable);
}
