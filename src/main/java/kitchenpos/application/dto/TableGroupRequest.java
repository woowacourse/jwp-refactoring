package kitchenpos.application.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest() {
    }

    public TableGroup toEntity() {
        return new TableGroup(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
