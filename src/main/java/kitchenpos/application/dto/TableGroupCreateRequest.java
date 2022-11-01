package kitchenpos.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
