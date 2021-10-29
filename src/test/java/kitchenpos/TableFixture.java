package kitchenpos;

import kitchenpos.domain.OrderTable;

public class TableFixture {

    private static final int NUMBER_OF_GUEST = 10;

    public static OrderTable createOrderTable() {
        return createOrderTable(null);
    }

    public static OrderTable createOrderTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(NUMBER_OF_GUEST);
        orderTable.setEmpty(false);
        orderTable.setId(id);
        return orderTable;
    }
}
