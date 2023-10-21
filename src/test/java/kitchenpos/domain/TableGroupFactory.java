package kitchenpos.domain;

import java.util.ArrayList;
import kitchenpos.domain.ordertable.OrderTable;

public final class TableGroupFactory {

    private TableGroupFactory() {
    }

    public static TableGroup createTableGroupTableOf(final OrderTable... orderTables) {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>());
        for (final var orderTable : orderTables) {
            tableGroup.getOrderTables().add(orderTable);
        }
        return tableGroup;
    }
}
