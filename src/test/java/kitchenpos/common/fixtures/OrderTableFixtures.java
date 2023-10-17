package kitchenpos.common.fixtures;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableCreateRequest;

public class OrderTableFixtures {

    /**
     * NUMBER_OF_GUESTS
     */
    public static final int ORDER_TABLE1_NUMBER_OF_GUESTS = 0;

    /**
     * IS_EMPTY
     */
    public static final boolean ORDER_TABLE1_IS_EMPTY = true;

    /**
     * CREATE_REQUEST
     */
    public static OrderTableCreateRequest ORDER_TABLE1_CREATE_REQUEST() {
        return new OrderTableCreateRequest(ORDER_TABLE1_NUMBER_OF_GUESTS, ORDER_TABLE1_IS_EMPTY);
    }

    /**
     * ENTITY
     */
    public static OrderTable ORDER_TABLE1() {
        return new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, ORDER_TABLE1_IS_EMPTY);
    }
}
