package kitchenpos.tablegroup.presentation.dto.request;

import java.util.List;
import kitchenpos.ordertable.presentation.dto.request.OrderTableRequest;

public class CreateTableGroupRequest {

    private List<OrderTableRequest> orderTables;

    private CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
