package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;

public interface TableValidator {

    void validateEmpty(Long orderTableId);

    void validateTablesForGroup(List<OrderTable> orderTables);

    void validateUpGroup(List<OrderTable> orderTables);
}
