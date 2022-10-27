package kitchenpos.support.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createNonEmptyStatusTable() {
        return new OrderTable(null, 1, false);
    }

    public static OrderTable createEmptyStatusTable() {
        return new OrderTable(null, 1, true);
    }
}
