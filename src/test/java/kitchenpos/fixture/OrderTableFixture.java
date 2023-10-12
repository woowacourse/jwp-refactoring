package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 주문테이블() {
        return new OrderTable();
    }

    public static OrderTable 빈테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable 비지않은_테이블() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable 주문테이블(final int numbersOfGuest) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numbersOfGuest);
        return orderTable;
    }
}
