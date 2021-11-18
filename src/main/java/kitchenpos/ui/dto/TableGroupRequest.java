package kitchenpos.ui.dto;

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
