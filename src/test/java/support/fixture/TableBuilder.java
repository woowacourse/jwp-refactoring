package support.fixture;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;

public class TableBuilder {

    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public TableBuilder() {
        this.tableGroup = null;
        this.numberOfGuests = 0;
        this.empty = true;
    }

    public TableBuilder setTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        return this;
    }

    public TableBuilder setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        return this;
    }

    public TableBuilder setEmpty(final boolean empty) {
        this.empty = empty;
        return this;
    }

    public OrderTable build() {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}
