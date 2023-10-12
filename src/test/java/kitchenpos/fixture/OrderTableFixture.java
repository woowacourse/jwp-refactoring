package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable 테이블(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable 테이블(boolean empty, int numberOfGuests, long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        return orderTable;
    }
}
