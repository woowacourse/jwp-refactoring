package kitchenpos.table.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;
import java.util.List;

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
