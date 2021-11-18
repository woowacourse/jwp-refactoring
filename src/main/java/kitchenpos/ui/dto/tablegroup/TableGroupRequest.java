package kitchenpos.ui.dto.tablegroup;

import kitchenpos.ui.dto.order.OrderTableIdRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TableGroupRequest {

    @NotNull
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
