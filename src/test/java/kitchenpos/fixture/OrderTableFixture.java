package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createDefaultWithoutId() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);
        return orderTable;
    }

    public static OrderTable create(final boolean isEmpty, final Integer guestCount) {
        final OrderTable orderTable = createDefaultWithoutId();
        orderTable.setNumberOfGuests(guestCount);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
