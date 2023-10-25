package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
