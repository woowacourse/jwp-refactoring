package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final int NUMBER_OF_GUEST = 10;
    public static final int INVALID_NUMBER_OF_GUEST = -1;
    public static final OrderTable UNSAVED_ORDER_TABLE_EMPTY = new OrderTable(10, true);
    public static final OrderTable UNSAVED_ORDER_TABLE_NOT_EMPTY = new OrderTable(10, false);
    public static final OrderTable SAVED_ORDER_TABLE_NOT_EMPTY_FIRST = new OrderTable(1L, null, 0, false);
    public static final OrderTable SAVED_ORDER_TABLE_NOT_EMPTY_SECOND = new OrderTable(2L, null, 0, false);
    public static final OrderTable SAVED_ORDER_TABLE_EMPTY_FIRST = new OrderTable(3L, null, 0, true);
    public static final OrderTable SAVED_ORDER_TABLE_EMPTY_SECOND = new OrderTable(4L, null, 0, true);

    public static OrderTable makeOrderTable(int numberOfGuest, boolean empty, Long groupId) {
        OrderTable orderTable = new OrderTable(null, groupId, numberOfGuest, empty);
        return orderTable;
    }
}
