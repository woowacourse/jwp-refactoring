package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {
    public static TableGroup createTableGroupRequest(List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable() {{
                    setId(it);
                }})
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup createTableGroup(List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable() {{
                    setId(it);
                }})
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
