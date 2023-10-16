package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public final class OrderTableFixture {

    public static OrderTable NOT_EMPTY_테이블() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        return orderTable;
    }

    public static OrderTable EMPTY_테이블() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(5);
        return orderTable;
    }
}
