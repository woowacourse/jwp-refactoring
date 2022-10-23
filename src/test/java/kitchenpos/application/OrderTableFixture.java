package kitchenpos.application;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

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
}
