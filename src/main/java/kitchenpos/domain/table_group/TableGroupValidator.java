package kitchenpos.domain.table_group;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.AggregateReference;

public interface TableGroupValidator {

    void validate(final List<AggregateReference<OrderTable>> orderTables);

    void validateUnGroup(final AggregateReference<OrderTable> orderTable);
}
