package kitchenpos.fixtures;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable ORDER_TABLE() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }
}
