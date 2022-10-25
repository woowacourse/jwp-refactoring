package kitchenpos.common.builder;

import kitchenpos.domain.OrderTable;

public class OrderTableBuilder {

    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableBuilder tableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        return this;
    }

    public OrderTableBuilder numberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public OrderTableBuilder empty(final boolean empty) {
        this.empty = empty;
        return this;
    }
    
    public OrderTable build() {
        return new OrderTable(numberOfGuests, empty);
    }
}
