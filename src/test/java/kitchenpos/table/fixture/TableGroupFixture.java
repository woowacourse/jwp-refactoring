package kitchenpos.table.fixture;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup createTableGroup(Long id) {
        return TableGroup.builder()
                .id(id)
                .build();
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return TableGroup.builder()
                .orderTables(orderTables)
                .build();
    }

    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return TableGroup.builder()
                .createdDate(createdDate)
                .orderTables(orderTables)
                .build();
    }
}
