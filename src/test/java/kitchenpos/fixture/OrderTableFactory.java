package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFactory {

    public static OrderTable emptyTable(final int guests) {
        return new OrderTable(null, null, guests, true);
    }

    public static OrderTable emptyTable(final Long id, final int guests) {
        return new OrderTable(id, null, guests, true);
    }

    public static OrderTable notEmptyTable(final int guests) {
        return new OrderTable(null, null, guests, false);
    }
}
