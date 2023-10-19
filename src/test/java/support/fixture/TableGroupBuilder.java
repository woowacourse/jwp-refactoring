package support.fixture;

import kitchenpos.domain.order_table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;

import java.util.ArrayList;
import java.util.List;

public class TableGroupBuilder {

    private List<OrderTable> orderTables;

    public TableGroupBuilder() {
        this.orderTables = new ArrayList<>();
    }

    public TableGroupBuilder setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup(orderTables);
    }
}
