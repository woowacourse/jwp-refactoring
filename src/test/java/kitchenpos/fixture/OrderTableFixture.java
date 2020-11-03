package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(Long id, int numberOfGuests, Long tableGroupId, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createOrderTableWithEmpty(boolean empty) {
        return createOrderTable(null, 2, null, empty);
    }

    public static OrderTable createOrderTableWithTableGroupId(Long tableGroupId) {
        return createOrderTable(null, 2, tableGroupId, false);
    }

    public static OrderTable createOrderTableWithTableGroupIdAndEmpty(Long tableGroupId, boolean empty) {
        return createOrderTable(null, 2, tableGroupId, empty);
    }

    public static OrderTable createOrderTableWithNumberOfGuest(int numberOfGuest) {
        return createOrderTable(null, numberOfGuest, null, false);
    }

    public static OrderTable createOrderTableWithId(Long id) {
        return createOrderTable(id, 2, null, true);
    }
}
