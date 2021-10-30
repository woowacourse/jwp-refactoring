package kitchenpos.dto.request;

import java.util.List;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class CreateTableGroupRequest {
    private List<OrderTable> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toEntity() {
        return new TableGroup(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
