package kitchenpos.table.domain;


import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void groupByTableGroup(final TableGroup tableGroup, final OrderTablesValidator orderTablesValidator) {
        orderTablesValidator.validateSize(orderTables);
        orderTablesValidator.validateGroupOrderTableIsAvailable(orderTables);
        orderTables.forEach(orderTable -> orderTable.groupByTableGroup(tableGroup));
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
