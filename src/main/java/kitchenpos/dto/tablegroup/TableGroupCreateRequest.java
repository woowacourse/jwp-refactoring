package kitchenpos.dto.tablegroup;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import kitchenpos.dto.table.OrderTableFindRequest;

public class TableGroupCreateRequest {

    @Size(min = 2)
    @NotEmpty
    private final List<OrderTableFindRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableFindRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableFindRequest> getOrderTables() {
        return orderTables;
    }
}
