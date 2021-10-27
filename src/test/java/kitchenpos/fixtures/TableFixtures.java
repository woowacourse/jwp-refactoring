package kitchenpos.fixtures;

import kitchenpos.domain.OrderTable;

public class TableFixtures {

    private static final long TABLE_GROUP_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 10;
    private static final boolean EMPTY = false;
    private static final long ORDER_TABLE_ID = 1L;

    public static OrderTable createOrderTable(
        Long id,
        Long tableGroupId,
        int numberOfGuests,
        boolean empty
    ) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createOrderTable() {
        return createOrderTable(ORDER_TABLE_ID, TABLE_GROUP_ID, NUMBER_OF_GUESTS, EMPTY);
    }

}
