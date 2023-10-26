package kitchenpos.ordertable.application.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class TableGroupCreateRequest {

    @Size(min = 2)
    @NotEmpty
    private List<OrderTableFindRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableFindRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableFindRequest> getOrderTables() {
        return orderTables;
    }
}
