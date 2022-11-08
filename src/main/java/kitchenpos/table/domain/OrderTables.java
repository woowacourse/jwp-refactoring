package kitchenpos.table.domain;

import java.util.List;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void group(final Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            orderTable.addTableId(tableGroupId);
        }
    }
}
