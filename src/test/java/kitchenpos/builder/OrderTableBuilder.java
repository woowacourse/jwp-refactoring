package kitchenpos.builder;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableBuilder {

    private Long id;
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableBuilder tableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        return this;
    }

    public OrderTableBuilder numberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableBuilder empty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTable build() {
        return new OrderTable(this.id, this.tableGroup, this.numberOfGuests, this.empty);
    }
}
