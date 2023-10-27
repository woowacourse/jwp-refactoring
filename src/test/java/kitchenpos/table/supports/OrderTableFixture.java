package kitchenpos.table.supports;

import kitchenpos.table.domain.model.OrderTable;

public class OrderTableFixture {

    private Long id = null;
    private Long tableGroupId = 1L;
    private int numberOfGuests = 4;
    private boolean empty = false;

    private OrderTableFixture() {
    }

    public static OrderTableFixture fixture() {
        return new OrderTableFixture();
    }

    public OrderTableFixture id(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableFixture tableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    public OrderTableFixture numberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableFixture empty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTable build() {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
