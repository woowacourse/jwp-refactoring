package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

public final class TableGroupFactory {

    private TableGroupFactory() {
    }

    public static TableGroup createTableGroupTableOf(final List<OrderTable> orderTables) {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>());
        for (final var orderTable : orderTables) {
            tableGroup.getOrderTables().add(orderTable);
        }
        return tableGroup;
    }

    public static TableGroup empty(final long id) {
        final var tableGroup = new TableGroup();
        tableGroup.setId(id);
        return tableGroup;
    }
}
