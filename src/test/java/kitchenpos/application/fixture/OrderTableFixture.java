package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final Long id, final int guests) {
        return new OrderTable(id, guests, false);
    }

    public static OrderTable createOrderTable(final Long id, final Long tableGroupId, final int guests) {
        return new OrderTable(id, tableGroupId, guests, false);
    }
}
