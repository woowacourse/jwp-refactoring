package kitchenpos.dto;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(null, LocalDateTime.now());
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
