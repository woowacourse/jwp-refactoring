package kitchenpos.tablegroup.ui.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.List;

public class CreateTableGroupRequest {

    private List<OrderTable> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
