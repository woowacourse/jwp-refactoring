package kitchenpos.table_group.application.dto.request;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table_group.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableReferenceRequest> orderTables;

    public TableGroupCreateRequest(final List<OrderTableReferenceRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup(final OrderTables orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    public TableGroupCreateRequest() {
    }

    public List<OrderTableReferenceRequest> getOrderTables() {
        return orderTables;
    }
}
