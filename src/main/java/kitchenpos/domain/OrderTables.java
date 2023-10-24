package kitchenpos.domain;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void verify() {
        if (orderTables.stream().anyMatch(savedOrderTable -> !savedOrderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(OrderTable::checkTableGroup);
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.setTableGroup(tableGroup);
        }
    }
}
