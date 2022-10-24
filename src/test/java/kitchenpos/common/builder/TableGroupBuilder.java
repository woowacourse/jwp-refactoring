package kitchenpos.common.builder;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupBuilder {

    private List<OrderTable> orderTables;

    public TableGroupBuilder orderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(orderTables);
    }
}
