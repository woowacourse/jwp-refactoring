package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    private static final Long ID = 1L;
    private static final Long TABLE_GROUP_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 1;
    private static final boolean EMPTY = false;

    public static OrderTable createOrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createOrderTable() {
        return createOrderTable(ID, TABLE_GROUP_ID, NUMBER_OF_GUESTS, EMPTY);
    }

    public static OrderTable createOrderTable(int numberOfGuests) {
        return createOrderTable(ID, TABLE_GROUP_ID, numberOfGuests, EMPTY);
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, boolean empty) {
        return createOrderTable(id, tableGroupId, NUMBER_OF_GUESTS, empty);
    }

    public static OrderTable createOrderTable(boolean isEmpty) {
        return createOrderTable(ID, TABLE_GROUP_ID, NUMBER_OF_GUESTS, isEmpty);
    }
}
