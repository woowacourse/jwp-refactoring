package kitchenpos.table.domain;

import java.util.List;

public interface TableGroupValidator {

    void validateCreateGroup(List<OrderTable> orderTables);

    void validateUnGroup(List<Long> orderTableIds);
}
