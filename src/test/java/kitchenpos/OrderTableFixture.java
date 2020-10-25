package kitchenpos;

import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.OrderTable;

public class OrderTableFixture {
    public static OrderTable createOrderTableWithoutId() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(null);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable createOrderTableWithId(final Long orderTableId) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(2);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static List<OrderTable> createOrderTables() {
        return Arrays.asList(createOrderTableWithId(1L), createOrderTableWithId(2L));
    }
}
