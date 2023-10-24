package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class TableGroupCreateRequest {

    private final List<TableGroupTableRequest> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(List<TableGroupTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupTableRequest> getOrderTables() {
        return orderTables;
    }
}
