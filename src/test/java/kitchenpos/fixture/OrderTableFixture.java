package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable newOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        final var orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.updateNumberOfGuests(numberOfGuests);
        orderTable.changeEmptyStatusTo(empty);
        return orderTable;
    }
}
