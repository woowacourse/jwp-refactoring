package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableDto> orderTables;

    public TableGroupCreateRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
