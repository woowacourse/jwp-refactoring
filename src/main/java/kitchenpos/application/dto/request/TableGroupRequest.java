package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<SavedOrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<SavedOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<SavedOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
