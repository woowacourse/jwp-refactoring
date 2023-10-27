package fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class OrderTableBuilder {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public static OrderTableBuilder init() {
        final OrderTableBuilder builder = new OrderTableBuilder();
        builder.id = null;
        builder.tableGroupId = null;
        builder.numberOfGuests = 2;
        builder.empty = false;
        return builder;
    }

    public OrderTableBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public OrderTableBuilder tableGroup(Long tableGroupId) {
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
        return new OrderTable(
                id,
                tableGroupId,
                numberOfGuests,
                empty
        );
    }
}

