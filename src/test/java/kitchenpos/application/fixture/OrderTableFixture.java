package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable createOrderTable(Long id, boolean empty, Long tableGroupId, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable createOrderTableRequest(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable modifyOrderTableStatusRequest(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable modifyOrderTableNumOfGuestRequest(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
