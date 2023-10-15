package kitchenpos.ui.dto.tablegroup;

import java.util.List;
import kitchenpos.ui.dto.ordertable.OrderTableIdDto;

public class TableGroupCreateRequest {

    private List<OrderTableIdDto> orderTables;

    TableGroupCreateRequest() {

    }

    public TableGroupCreateRequest(final List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
