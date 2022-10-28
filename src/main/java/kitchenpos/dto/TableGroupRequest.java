package kitchenpos.dto;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private OrderTables orderTables;

    public TableGroupRequest(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(orderTables);
    }
}
