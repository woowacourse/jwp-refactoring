package kitchenpos.builder;

import kitchenpos.domain.OrderTable;

public class OrderTableBuilder {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableBuilder() {
    }

    public OrderTableBuilder(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableBuilder tableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
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
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }
}
