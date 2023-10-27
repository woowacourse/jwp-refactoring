package kitchenpos.tablegroup.dto;

import java.util.List;
import kitchenpos.order.dto.OrderTableInTableGroupDto;

public class TableGroupCreateRequest {

    private final List<OrderTableInTableGroupDto> orderTables;

    public TableGroupCreateRequest(final List<OrderTableInTableGroupDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableInTableGroupDto> getOrderTables() {
        return orderTables;
    }
}
