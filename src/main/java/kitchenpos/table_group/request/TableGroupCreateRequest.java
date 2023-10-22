package kitchenpos.table_group.request;

import java.util.List;
import kitchenpos.order.request.OrderTableDto;

public class TableGroupCreateRequest {

    private final List<OrderTableDto> orderTables;

    public TableGroupCreateRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
