package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public final class OrderTableFixture {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableFixture() {
    }

    public static OrderTableFixture builder() {
        return new OrderTableFixture();
    }

    public OrderTableFixture withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableFixture withTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    public OrderTableFixture withNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableFixture withEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTable build() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }
}
