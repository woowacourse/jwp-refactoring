package kitchenpos.fixtures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.OrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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

    public static OrderTableRequest createOrderTableRequest(OrderTable request) {
        return new OrderTableRequest(request.getNumberOfGuests(), request.isEmpty());
    }

    public static OrderTableRequest createOrderTableRequest(boolean empty) {
        return createOrderTableRequest(createOrderTable(empty));
    }

    public static List<OrderTable> createOrderTables(boolean empty) {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(createOrderTable(empty));
        orderTables.add(createOrderTable(2L, null, NUMBER_OF_GUESTS, empty));
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
        return new TableGroupRequest(tableGroup.getOrderTables());
    }

    public static TableGroupRequest createTableGroupRequest() {
        return createTableGroupRequest(createTableGroup());
    }
}
