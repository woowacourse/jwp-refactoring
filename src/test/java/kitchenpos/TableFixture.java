package kitchenpos;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableFixture {

    private static final int NUMBER_OF_GUEST = 10;

    public static OrderTable createOrderTable() {
        return createOrderTable(false);
    }

    public static OrderTable createOrderTable(TableGroup tableGroup) {
        return new OrderTable(tableGroup, NUMBER_OF_GUEST, false);
    }

    public static OrderTable createOrderTable(boolean empty) {
        return new OrderTable(NUMBER_OF_GUEST, empty);
    }

    public static TableGroup createTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> newOrderTables = orderTables.stream()
                .map(it -> createOrderTable())
                .collect(Collectors.toList());
        tableGroup.changeOrderTables(newOrderTables);
        return tableGroup;
    }
}
