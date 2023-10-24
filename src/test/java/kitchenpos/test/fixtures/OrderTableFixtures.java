package kitchenpos.test.fixtures;

import kitchenpos.ordertable.domain.OrderTable;

public enum OrderTableFixtures {
    EMPTY(0L, 0, true),
    NOT_EMPTY(2L, 2, false),
    BASIC(1L, 1, true);

    private final long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    OrderTableFixtures(final long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable get() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroup(tableGroupId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        orderTable.changeEmpty(empty);
        return orderTable;
    }
}
