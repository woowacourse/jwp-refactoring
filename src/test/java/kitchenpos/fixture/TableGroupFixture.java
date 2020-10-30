package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup createTableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup createTableGroupWithOrderTables(List<OrderTable> orderTables) {
        return createTableGroup(null, orderTables);
    }

    public static TableGroup createTableGroupWithoutId() {
        return createTableGroup(null, null);
    }
}
