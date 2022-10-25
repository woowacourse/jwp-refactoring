package kitchenpos.support.fixture.domain;

import kitchenpos.domain.OrderTable;

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
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public OrderTable getOrderTable(Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public OrderTable getOrderTable(Long id, Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
