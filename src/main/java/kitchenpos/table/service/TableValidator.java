package kitchenpos.table.service;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface TableValidator {
    void validateUpdateEmpty(OrderTable orderTable);

    void validateUngroup(List<OrderTable> orderTables);
}
