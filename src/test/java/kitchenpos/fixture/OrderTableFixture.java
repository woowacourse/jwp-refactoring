package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    private static final OrderTable orderTable = new OrderTable(0L, 0L, 0, false);
    private static final OrderTable notGroupedTable = new OrderTable(0L, null, 0, true);

    public static OrderTable orderTable() {
        return orderTable;
    }

    public static OrderTable notGroupedTable() {
        return notGroupedTable;
    }
}
