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
        TableGroup tableGroup,
        List<Order> orders,
        int numberOfGuests,
        boolean empty
    ) {
        return new OrderTable(id, tableGroup, orders, numberOfGuests, empty);
    }

    public static OrderTable createOrderTable(boolean empty) {
        return createOrderTable(ORDER_TABLE_ID, null, createCompletedOrders(), NUMBER_OF_GUESTS, empty);
    }

    public static OrderTable createGroupedOrderTable(boolean empty) {
        return createOrderTable(ORDER_TABLE_ID, createTableGroup(), createCompletedOrders(), NUMBER_OF_GUESTS, empty);
    }

    public static OrderTableRequest createOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<OrderTable> createOrderTables(boolean empty) {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(createOrderTable(empty));
        orderTables.add(createOrderTable(2L, null, createCompletedOrders(), NUMBER_OF_GUESTS, empty));
        return orderTables;
    }

    public static TableGroup createTableGroup(
        Long id,
        LocalDateTime createdDate,
        List<OrderTable> orderTables
    ) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup createTableGroup() {
        return createTableGroup(TABLE_GROUP_ID, LocalDateTime.now(), createOrderTables(true));
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        return createTableGroup(TABLE_GROUP_ID, LocalDateTime.now(), orderTables);
    }

    public static TableGroupRequest createTableGroupRequest(TableGroup tableGroup) {
        return new TableGroupRequest(
            tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList())
        );
    }

    public static TableGroupRequest createTableGroupRequest() {
        return createTableGroupRequest(createTableGroup());
    }
}
