package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {
    public static OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 테이블_1번() {
        return createOrderTable(0, true);
    }

    public static OrderTable 테이블_2번() {
        return createOrderTable(0, true);
    }

    public static OrderTable 테이블_3번() {
        return createOrderTable(0, true);
    }
}
