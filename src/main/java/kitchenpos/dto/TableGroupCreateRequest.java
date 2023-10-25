package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableInTableGroupDto> orderTables;

    public TableGroupCreateRequest(final List<OrderTableInTableGroupDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableInTableGroupDto> getOrderTables() {
        return orderTables;
    }
}
