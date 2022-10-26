package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable() {
        return new OrderTable();
    }

    public static OrderTable createOrderTable(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }

    public static OrderTable createOrderTable(boolean empty) {
        return createOrderTable(0, empty);
    }

    public static OrderTable createOrderTable(int numberOfGuests) {
        return createOrderTable(numberOfGuests, true);
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
