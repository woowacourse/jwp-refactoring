package kitchenpos.dto;

import com.sun.istack.NotNull;
import java.util.List;

public class TableGroupRequest {
    @NotNull
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
