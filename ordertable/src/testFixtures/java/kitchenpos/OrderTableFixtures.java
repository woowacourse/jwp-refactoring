package kitchenpos;

import kitchenpos.domain.OrderTable;

public class OrderTableFixtures {

    private OrderTableFixtures() {
    }

    public static OrderTable create() {
        return new OrderTable(5, true);
    }

    public static OrderTable createWithNotEmpty() {
        return new OrderTable(5, false);
    }

    public static OrderTable createWithTableGroup(final Long tableGroupId) {
        return new OrderTable(null, tableGroupId, 5, true);
    }
}
