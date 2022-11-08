package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface Validator {

    void validateOrderStatus(final OrderTable orderTable);

    void validateOrdersStatus(final List<OrderTable> orderTables);
}
