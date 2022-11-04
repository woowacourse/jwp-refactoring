package kitchenpos.common.builder;

import java.util.List;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupBuilder {

    private List<OrderTable> orderTables;

    public TableGroupBuilder orderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        return this;
    }

    public TableGroup build() {
        return new TableGroup();
    }
}
