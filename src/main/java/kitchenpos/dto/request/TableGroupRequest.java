package kitchenpos.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<IdRequest> orderTables;

    public TableGroupRequest(final List<IdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<IdRequest> getOrderTables() {
        return orderTables;
    }

    private TableGroupRequest() {
    }
}
