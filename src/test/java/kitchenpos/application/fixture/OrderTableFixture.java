package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final int NUMBER_OF_GUEST = 10;
    public static final int INVALID_NUMBER_OF_GUEST = -1;
    public static final OrderTable UNSAVED_ORDER_TABLE_EMPTY = new OrderTable(10, true);
    public static final OrderTable UNSAVED_ORDER_TABLE_NOT_EMPTY = new OrderTable(10, false);
    public static final OrderTable SAVED_ORDER_TABLE_NOT_EMPTY_FIRST = new OrderTable(0, false);
    public static final OrderTable SAVED_ORDER_TABLE_NOT_EMPTY_SECOND = new OrderTable(0, false);
    public static final OrderTable SAVED_ORDER_TABLE_EMPTY_FIRST = new OrderTable(0, true);
    public static final OrderTable SAVED_ORDER_TABLE_EMPTY_SECOND = new OrderTable(0, true);


    static {
        SAVED_ORDER_TABLE_NOT_EMPTY_FIRST.setId(1L);
        SAVED_ORDER_TABLE_NOT_EMPTY_SECOND.setId(2L);
        SAVED_ORDER_TABLE_EMPTY_FIRST.setId(3L);
        SAVED_ORDER_TABLE_EMPTY_SECOND.setId(4L);
    }

    public static OrderTable makeOrderTable(int numberOfGuest, boolean empty, Long groupId) {
        OrderTable orderTable = new OrderTable(numberOfGuest, empty);
        orderTable.setTableGroupId(groupId);
        return orderTable;
    }
}
