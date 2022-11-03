package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTable> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(new OrderTables(orderTables));
    }
}
