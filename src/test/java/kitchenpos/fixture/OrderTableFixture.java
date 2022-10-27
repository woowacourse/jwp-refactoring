package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable generateOrderTable(Long tableGroupId, int numberOfGuest, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
