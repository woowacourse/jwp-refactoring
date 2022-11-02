package kitchenpos.ui.jpa.dto.tablegroup;

import java.util.List;
import kitchenpos.domain.entity.OrderTable;

public class TableGroupCreateRequest {

    private List<OrderTable> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
