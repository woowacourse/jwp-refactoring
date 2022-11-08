package kitchenpos.table.fixture;

import kitchenpos.table.domain.OrderTable;

public class OrderTableFixture {

    public static OrderTable createOrderTable() {
        return new OrderTable();
    }

    public static OrderTable createOrderTable(Long id) {
        return OrderTable.builder()
                .id(id)
                .build();
    }

    public static OrderTable createOrderTable(boolean empty) {
        return createOrderTable(0, empty);
    }

    public static OrderTable createOrderTable(int numberOfGuests) {
        return createOrderTable(numberOfGuests, true);
    }

    public static OrderTable createOrderTable(int numberOfGuests, boolean empty) {
        return OrderTable.builder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }

    public static OrderTable createOrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.builder()
                .tableGroupId(tableGroupId)
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }
}
