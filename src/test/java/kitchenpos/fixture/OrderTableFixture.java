package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable createOrderTable(Long id, boolean empty, int numberOfGuests,
        Long tableGroupId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(tableGroupId);

        return orderTable;
    }

    public static OrderTable createOrderTableRequest(boolean empty, int numberOfGuests) {
        return createOrderTable(null, empty, numberOfGuests, null);
    }
}
