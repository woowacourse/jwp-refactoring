package kitchenpos.application.fixture;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

public abstract class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static OrderTable orderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
}
