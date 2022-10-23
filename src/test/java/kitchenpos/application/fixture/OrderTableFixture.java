package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    public static OrderTable forUpdateEmpty(final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);

        return orderTable;
    }

    public static OrderTable forUpdateGuestNumber(final int numberOfGuests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);

        return orderTable;
    }
}
