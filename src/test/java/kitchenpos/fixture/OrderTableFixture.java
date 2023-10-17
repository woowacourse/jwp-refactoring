package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable create(boolean emptyStatus, int guestCount) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        orderTable.setNumberOfGuests(guestCount);

        return orderTable;
    }

}
