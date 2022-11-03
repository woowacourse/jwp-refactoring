package kitchenpos.support;

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;

public enum OrderTableFixture {

    ORDER_TABLE_NOT_EMPTY_1(2, false),
    ORDER_TABLE_EMPTY_1(2, true),
    ;

    private final int numberOfGuest;
    private final boolean empty;

    OrderTableFixture(int numberOfGuest, boolean empty) {
        this.numberOfGuest = numberOfGuest;
        this.empty = empty;
    }

    public OrderTable 생성() {
        return new OrderTable(null, new NumberOfGuests(numberOfGuest), empty);
    }
}
