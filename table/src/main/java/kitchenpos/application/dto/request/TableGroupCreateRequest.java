package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupCreateRequest {
    private List<TableGroupCreateOrderTableRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableGroupCreateOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupCreateOrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
