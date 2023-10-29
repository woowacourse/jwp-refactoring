package table.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableRequest> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
