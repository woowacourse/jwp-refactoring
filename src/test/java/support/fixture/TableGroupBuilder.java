package support.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

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
        final OrderTables orderTables = new OrderTables(this.orderTables);
        return new TableGroup(orderTables);
    }
}
