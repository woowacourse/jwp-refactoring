package kitchenpos.tablegroup.application.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import kitchenpos.ordertable.application.dto.OrderTableFindRequest;

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
