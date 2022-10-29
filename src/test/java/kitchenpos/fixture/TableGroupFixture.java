package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupFixture {

    public static TableGroup newTableGroup(final LocalDateTime createdDate, final OrderTable... orderTables) {
        final var tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(List.of(orderTables));
        return tableGroup;
    }

    public static TableGroup newTableGroup(final OrderTable... orderTables) {
        return newTableGroup(LocalDateTime.now(), orderTables);
    }
}
