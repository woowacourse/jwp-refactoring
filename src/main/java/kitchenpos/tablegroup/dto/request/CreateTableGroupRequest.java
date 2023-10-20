package kitchenpos.tablegroup.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;
import java.util.List;
import kitchenpos.table.dto.request.OrderTableRequest;

public class CreateTableGroupRequest {

    @NotNull
    private List<OrderTableRequest> orderTables;

    @JsonCreator
    public CreateTableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
