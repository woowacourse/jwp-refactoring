package kitchenpos.support.fixture.domain;

import kitchenpos.table.domain.OrderTable;

public enum OrderTableFixture {

    GUEST_ONE_EMPTY_TRUE(1, true),
    GUEST_ONE_EMPTY_FALSE(1, false),
    GUEST_TWO_EMPTY_TRUE(2, true),
    GUEST_TWO_EMPTY_FALSE(2, false),
    ;

    private final int numberOfGuests;
    private final boolean empty;

    OrderTableFixture(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable getOrderTable() {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public OrderTable getOrderTable(Long tableGroupId) {
        return new OrderTable(tableGroupId, numberOfGuests, empty);
    }

    public OrderTable getOrderTable(Long id, Long tableGroupId) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
