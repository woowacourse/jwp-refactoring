package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    public static OrderTable EMPTY_TABLE() {
        return new OrderTable(0, true);
    }

    public static OrderTable NOT_EMPTY_TABLE() {
        return new OrderTable(0, false);
    }
}
