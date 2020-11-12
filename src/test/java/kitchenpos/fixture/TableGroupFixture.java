package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup createTableGroup(Long id, LocalDateTime createdDate,
        List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    public static TableGroup createTableGroupRequest(List<OrderTable> orderTables) {
        return createTableGroup(null, null, orderTables);
    }
}
