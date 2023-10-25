package kitchenpos.application.dto.tablegroup;

import java.util.List;
import kitchenpos.application.dto.ordertable.OrderTableIdDto;

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
