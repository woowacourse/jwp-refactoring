package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;

public final class OrderTableFixture {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty = true;

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
        return new OrderTable(
            id,
            tableGroupId,
            numberOfGuests,
            empty
        );
    }
}
