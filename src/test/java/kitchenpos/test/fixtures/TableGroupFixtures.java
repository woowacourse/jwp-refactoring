package kitchenpos.test.fixtures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public enum TableGroupFixtures {
    EMPTY(LocalDateTime.now(), Collections.emptyList()),
    BASIC(LocalDateTime.now(), new ArrayList<>(List.of(OrderTableFixtures.BASIC.get(), OrderTableFixtures.BASIC.get())));

    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    TableGroupFixtures(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup get() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
