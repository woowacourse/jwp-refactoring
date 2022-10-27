package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFactory {

    public static OrderTable emptyTable(final Long id, final int guests) {
        final var table = new OrderTable(guests);
        table.setId(id);
        table.setEmpty(true);

        return table;
    }

    public static OrderTable emptyTable(final int guests) {
        final var table = new OrderTable(guests);
        table.setEmpty(true);

        return table;
    }

    public static OrderTable notEmptyTable(final int guests) {
        final var table = new OrderTable(guests);
        table.setEmpty(false);

        return table;
    }
}
