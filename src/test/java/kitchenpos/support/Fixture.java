package kitchenpos.support;

import kitchenpos.table.domain.OrderTable;

public class Fixture {

    public static OrderTable createOrderTableWithNumberOfGuests(int numberOfGuests) {
        return new OrderTable(1L, null, numberOfGuests, false);
    }

    public static OrderTable createEmptyOrderTable() {
        return new OrderTable(1L, null, 0, true);
    }

    public static OrderTable createGroupedOrderTable() {
        return new OrderTable(1L, 1L, 0, true);
    }
}
