package kitchenpos.ui.dto.request;

import java.util.List;
import kitchenpos.ui.dto.OrderTableIdDto;

public class TableGroupCreateRequest {

    private List<OrderTableIdDto> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
