package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    public static OrderTable EMPTY_TABLE() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable NOT_EMPTY_TABLE() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        return orderTable;
    }
}
