package kitchenpos.application.dto.request;

import com.sun.istack.NotNull;
import java.util.List;

public class TableGroupRequest {
    @NotNull
    private List<OrderTableIdRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
