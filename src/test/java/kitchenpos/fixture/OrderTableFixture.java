package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    private static final Long ID = 1L;
    private static final Long TABLE_GROUP_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 1;
    private static final boolean EMPTY = false;

    public static OrderTable createOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ID);
        orderTable.setTableGroupId(TABLE_GROUP_ID);
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS);
        orderTable.setEmpty(EMPTY);
        return orderTable;
    }

    public static OrderTable createOrderTable(int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ID);
        orderTable.setTableGroupId(TABLE_GROUP_ID);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(EMPTY);
        return orderTable;
    }

    public static OrderTable createOrderTable(Long id, Long tableGroupId, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable createOrderTable(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(ID);
        orderTable.setTableGroupId(TABLE_GROUP_ID);
        orderTable.setNumberOfGuests(NUMBER_OF_GUESTS);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
