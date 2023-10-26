package kitchenpos.table_group.application.dto.request;

import kitchenpos.table_group.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableReferenceRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableReferenceRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }

    public TableGroupCreateRequest() {
    }

    public List<OrderTableReferenceRequest> getOrderTables() {
        return orderTables;
    }
}
