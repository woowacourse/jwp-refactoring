package kitchenpos.dto.table;

import java.util.List;

public class CreateTableGroupRequest {
    private final List<OrderTableRequest> orderTables;

    private CreateTableGroupRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static CreateTableGroupRequest of(final List<OrderTableRequest> orderTables) {
        return new CreateTableGroupRequest(orderTables);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
