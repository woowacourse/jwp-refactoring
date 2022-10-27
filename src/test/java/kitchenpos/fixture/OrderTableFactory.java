package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFactory {

    public static OrderTable emptyTable(final int guests) {
        final var table = new OrderTable();
        table.setEmpty(true);
        table.setNumberOfGuests(guests);

        return table;
    }

    public static OrderTable notEmptyTable(final int guests) {
        final var table = new OrderTable();
        table.setEmpty(false);
        table.setNumberOfGuests(guests);

        return table;
    }
}
