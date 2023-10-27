package kitchenpos.support.fixture.domain;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.domain.OrderTable;

public class TableGroupFixture {

    public static TableGroup getTableGroup() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    public static TableGroup getTableGroup(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);
        return tableGroup;
    }
}