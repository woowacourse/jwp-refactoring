package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    private static final OrderTable orderTable = new OrderTable(0L, 0L, 0, false);

    public static OrderTable orderTable() {
        return orderTable;
    }
}
