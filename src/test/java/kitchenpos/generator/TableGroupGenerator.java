package kitchenpos.generator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupGenerator {

    public static TableGroup newInstance(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableIds.stream()
            .map(id -> {
                OrderTable orderTable = new OrderTable();
                orderTable.setId(id);
                return orderTable;
            })
            .collect(Collectors.toList());
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    public static TableGroup newInstance(LocalDateTime createdDate) {
        return newInstance(null, createdDate);
    }

    public static TableGroup newInstance(Long id, LocalDateTime createdDate) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        return tableGroup;
    }
}
