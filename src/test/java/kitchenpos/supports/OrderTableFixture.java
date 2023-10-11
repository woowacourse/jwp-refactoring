package kitchenpos.supports;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createNotEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        return orderTable;
    }

    public static OrderTable createEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

}
