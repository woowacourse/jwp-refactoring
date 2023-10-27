package kitchenpos.table;

import ordertable.domain.OrderTable;

public final class OrderTableFactory {

    private OrderTableFactory() {
    }

    public static OrderTable createOrderTableOf(final int numberOfGuest, boolean empty) {
        return new OrderTable(numberOfGuest, empty);
    }
}
