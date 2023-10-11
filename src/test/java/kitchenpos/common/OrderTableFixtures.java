package kitchenpos.common;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    /**
     * NUMBER_OF_GUESTS
     */
    public static final int ORDER_TABLE1_NUMBER_OF_GUESTS = 0;
    public static final int ORDER_TABLE2_NUMBER_OF_GUESTS = 0;

    /**
     * IS_EMPTY
     */
    public static final boolean ORDER_TABLE1_IS_EMPTY = true;
    public static final boolean ORDER_TABLE2_IS_EMPTY = true;

    /**
     * REQUEST
     */
    public static OrderTable ORDER_TABLE1_REQUEST() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(ORDER_TABLE1_NUMBER_OF_GUESTS);
        orderTable.setEmpty(ORDER_TABLE1_IS_EMPTY);
        return orderTable;
    }

    public static OrderTable ORDER_TABLE2_REQUEST() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(ORDER_TABLE2_NUMBER_OF_GUESTS);
        orderTable.setEmpty(ORDER_TABLE2_IS_EMPTY);
        return orderTable;
    }
}
