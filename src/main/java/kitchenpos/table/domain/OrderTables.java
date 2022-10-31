package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.exception.InvalidGroupOrderTablesSizeException;

public class OrderTables {

    private static final int MINIMUM_ORDER_TABLES_SIZE = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_TABLES_SIZE) {
            throw new InvalidGroupOrderTablesSizeException();
        }
    }

    public <T> void validateSameSize(List<T> orderTablesIds) {
        if (orderTables.size() != orderTablesIds.size()) {
            throw new InvalidGroupOrderTablesSizeException();
        }
    }

    public void joinWithTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.occupyTableGroup(tableGroup.getId());
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.unOccupyTableGroup();
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
