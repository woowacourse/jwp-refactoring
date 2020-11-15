package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupFixture {
    public static TableGroup createTableGroupRequest(List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable(it, null, 1, false))
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup createTableGroup(Long id, LocalDateTime createdDateTime, List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDateTime);
        List<OrderTable> orderTables = orderTableIds.stream()
                .map(it -> new OrderTable(it, null, 1, false))
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
