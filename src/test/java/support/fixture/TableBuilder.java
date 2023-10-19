package support.fixture;

import kitchenpos.domain.order_table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;

public class TableBuilder {

    private final OrderTable table;

    public TableBuilder() {
        this.table = new OrderTable();
        table.setEmpty(true);
        table.setNumberOfGuests(0);
    }

    public TableBuilder setId(final Long id) {
        table.setId(id);
        return this;
    }

    public TableBuilder setTableGroup(final TableGroup tableGroup) {
        table.setTableGroup(tableGroup);
        return this;
    }

    public TableBuilder setNumberOfGuests(final int numberOfGuests) {
        table.setNumberOfGuests(numberOfGuests);
        return this;
    }

    public TableBuilder setEmpty(final boolean empty) {
        table.setEmpty(empty);
        return this;
    }

    public OrderTable build() {
        return table;
    }
}
