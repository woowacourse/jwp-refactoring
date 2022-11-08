package kitchenpos.table.dto.request;

import java.util.List;
import kitchenpos.table.dto.OrderTableDto;

public class TableGroupCreatRequest {

    private List<OrderTableDto> orderTables;

    public TableGroupCreatRequest() {
    }

    public TableGroupCreatRequest(final List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
