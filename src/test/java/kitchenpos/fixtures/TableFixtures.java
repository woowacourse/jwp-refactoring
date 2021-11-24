package kitchenpos.fixtures;

import static kitchenpos.fixtures.OrderFixtures.createCompletedOrders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.service.dto.OrderTableRequest;
import kitchenpos.tablegroup.service.dto.TableGroupRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableFixtures {

    private static final long TABLE_GROUP_ID = 1L;
    private static final int NUMBER_OF_GUESTS = 10;
    private static final long ORDER_TABLE_ID = 1L;

    public static OrderTable createOrderTable(
        Long id,
        Long tableGroupId,
        int numberOfGuests,
        boolean empty
    ) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(boolean empty) {
        return createOrderTable(ORDER_TABLE_ID, null, NUMBER_OF_GUESTS, empty);
    }

    public static OrderTable createGroupedOrderTable(boolean empty) {
        return createOrderTable(ORDER_TABLE_ID, createTableGroup().getId(), NUMBER_OF_GUESTS, empty);
    }

    public static OrderTableRequest createOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTable> createOrderTables(boolean empty) {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(createOrderTable(empty));
        orderTables.add(createOrderTable(2L, null, NUMBER_OF_GUESTS, empty));
        return orderTables;
    }

    public static TableGroup createTableGroup(
        Long id,
        LocalDateTime createdDate
    ) {
        return new TableGroup(id, createdDate);
    }

    public static TableGroup createTableGroup() {
        return createTableGroup(TABLE_GROUP_ID, LocalDateTime.now());
    }
}
