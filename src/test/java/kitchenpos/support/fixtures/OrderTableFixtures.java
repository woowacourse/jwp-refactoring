package kitchenpos.support.fixtures;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixtures {

    public static OrderTable createWithGuests(final Long tableGroupId, final int numberOfGuests) {
        return new OrderTable(null, tableGroupId, numberOfGuests, false);
    }

    public static OrderTable createEmptyTable(final Long tableGroupId) {
        return new OrderTable(null, tableGroupId, 0, true);
    }
}
