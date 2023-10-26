package kitchenpos.table.supports;

import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.model.TableGroup;

public class OrderTableFixture {

    private Long id = null;
    private TableGroup tableGroup = TableGroupFixture.fixture().build();
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

    public OrderTableFixture tableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
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
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
