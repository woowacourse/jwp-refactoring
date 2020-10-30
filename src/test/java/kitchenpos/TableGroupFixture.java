package kitchenpos;

import static kitchenpos.OrderTableFixture.*;

import java.time.LocalDateTime;

import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    public static TableGroup createTableGroupWithoutId() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(createEmptyOrderTables());
        return tableGroup;
    }

    public static TableGroup createTableGroupWithId(final Long tableGroupId) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(tableGroupId);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(createEmptyOrderTables());
        return tableGroup;
    }
}
