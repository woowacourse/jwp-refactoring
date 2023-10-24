package kitchenpos.common.fixtures;

import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;

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
     * CHANGE_EMPTY_REQUEST
     */
    public static OrderTableChangeEmptyRequest ORDER_TABLE1_CHANGE_EMPTY_REQUEST() {
        return new OrderTableChangeEmptyRequest(!ORDER_TABLE1_IS_EMPTY);
    }

    /**
     * CHANGE_GUEST_REQUEST
     */
    public static OrderTableChangeGuestRequest ORDER_TABLE1_CHANGE_GUEST_REQUEST() {
        return new OrderTableChangeGuestRequest(ORDER_TABLE1_NUMBER_OF_GUESTS + 1);
    }

    /**
     * ENTITY
     */
    public static OrderTable ORDER_TABLE1() {
        return new OrderTable(ORDER_TABLE1_NUMBER_OF_GUESTS, ORDER_TABLE1_IS_EMPTY);
    }
}
