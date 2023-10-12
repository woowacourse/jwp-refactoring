package kitchenpos.domain;

import java.util.ArrayList;

public final class TableGroupFactory {

    private TableGroupFactory() {
    }

    public static TableGroup createTableGroupTableOf(boolean isTableEmpty, final Long... orderTableIds) {
        final var tableGroup = new TableGroup();
        tableGroup.setOrderTables(new ArrayList<>());
        for (final var orderTableId : orderTableIds) {
            final var orderTable = OrderTableFactory.createOrderTableFrom(orderTableId);
            orderTable.setEmpty(isTableEmpty);
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
