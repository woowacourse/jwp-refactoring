package kitchenpos.application.fixture;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTables;
import kitchenpos.domain.order.TableGroup;

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
