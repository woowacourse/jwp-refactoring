package kitchenpos.support.fixture.domain;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable getOrderTable(final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable getOrderTable(final int numberOfGuests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }

    public static OrderTable getOrderTable(final Long tableGroupId, final int numberOfGuests, final boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
