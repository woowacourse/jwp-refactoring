package kitchenpos.ordertable.dto;

import java.util.List;
import kitchenpos.common.dto.IdRequest;

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
