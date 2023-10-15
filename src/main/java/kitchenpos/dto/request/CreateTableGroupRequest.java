package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class CreateTableGroupRequest {

    private List<OrderTableRequest> orderTables;

    @JsonCreator
    public CreateTableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
