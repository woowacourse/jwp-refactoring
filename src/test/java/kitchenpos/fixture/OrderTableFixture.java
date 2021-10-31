package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable orderTable() {
        return new OrderTable(0L, 0L, 0, false);
    }

    public static OrderTable notGroupedTable() {
        return new OrderTable(0L, null, 0, true);
    }
}
