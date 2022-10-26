package kitchenpos.support;

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
        return new OrderTable(null, numberOfGuest, empty);
    }

    public OrderTable 생성(final long tableGroupId) {
        return new OrderTable(tableGroupId, numberOfGuest, empty);
    }
}
