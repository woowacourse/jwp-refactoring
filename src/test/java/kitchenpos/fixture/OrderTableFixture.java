package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable ORDER_TABLE(boolean empty, int numberOfGuests) {
        return new OrderTable(
                null,
                numberOfGuests,
                empty
        );
    }
}
