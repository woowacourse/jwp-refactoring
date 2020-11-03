package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {
    static TableGroup createTableGroupRequest(List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable() {{
                    setId(it);
                }})
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    static TableGroup createTableGroup(List<Long> orderTableIds) {
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
