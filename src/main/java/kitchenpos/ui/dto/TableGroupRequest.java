package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {

    private List<TableGroupInnerOrderTableRequest> orderTableRequests;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<TableGroupInnerOrderTableRequest> orderTables) {
        this.orderTableRequests = orderTables;
    }

    public List<TableGroupInnerOrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
