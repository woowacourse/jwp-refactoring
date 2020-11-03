package kitchenpos.application;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    static OrderTable createOrderTable(Long id, boolean empty, Long tableGroupId, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    static OrderTable createOrderTableRequest(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    static OrderTable modifyOrderTableStatusRequest(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(empty);
        return orderTable;
    }

    static OrderTable modifyOrderTableNumOfGuestRequest(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
