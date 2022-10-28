package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean isEmpty) {
        return new OrderTable(numberOfGuests, isEmpty);
    }
}
