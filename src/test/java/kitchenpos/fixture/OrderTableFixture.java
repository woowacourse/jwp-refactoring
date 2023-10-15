package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable orderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }

    public static OrderTable orderTable(final Long tableGroupId, final OrderTable orderTable) {
        orderTable.setTableGroupId(tableGroupId);

        return orderTable;
    }
}
