package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable(final Long id, final int guests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(guests);

        return orderTable;
    }
}
