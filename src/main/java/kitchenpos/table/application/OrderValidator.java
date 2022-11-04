package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public interface OrderValidator {

    void validateUngroup(List<OrderTable> orderTables);

    void validateChangeEmpty(Long orderTableId);
}
