package kitchenpos.support.fixture.domain;

import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableFixture {

    private static final int EMPTY_GUEST_COUNT = 0;

    public static OrderTable getOrderTable(final boolean isEmpty) {
        return getOrderTable(null, EMPTY_GUEST_COUNT, isEmpty);
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final boolean isEmpty) {
        return getOrderTable(tableGroupId, EMPTY_GUEST_COUNT, isEmpty);
    }

    public static OrderTable getOrderTable(final int numberOfGuests, final boolean isEmpty) {
        return getOrderTable(null, numberOfGuests, isEmpty);
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean isEmpty) {
        return OrderTable.builder(numberOfGuests, isEmpty)
                .tableGroupId(tableGroupId)
                .build();
    }
}
