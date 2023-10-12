package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class CreateTableGroupRequest {

    private List<OrderTable> orderTables;

    public CreateTableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
