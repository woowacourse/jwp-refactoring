package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createDefaultWithoutId() {
        return new OrderTable(null, null, 0,null, true);
    }

    public static OrderTable create(final boolean isEmpty, final Integer guestCount) {
        return new OrderTable(null, null,guestCount, null, isEmpty );
    }
}
