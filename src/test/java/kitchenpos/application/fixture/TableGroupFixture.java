package kitchenpos.application.fixture;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public abstract class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static TableGroup tableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        final OrderTables tableGroupOrderTables = new OrderTables();
        tableGroupOrderTables.addAll(orderTables);

        return new TableGroup(createdDate, tableGroupOrderTables);
    }

    public static TableGroup tableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        return new TableGroup(createdDate, orderTables);
    }
}
