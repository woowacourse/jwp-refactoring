package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<TableGroupIdRequest> orderTableIds;

    private TableGroupCreateRequest() {
        this(null);
    }

    public TableGroupCreateRequest(List<TableGroupIdRequest> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<TableGroupIdRequest> getOrderTableIds() {
        return orderTableIds;
    }
}
