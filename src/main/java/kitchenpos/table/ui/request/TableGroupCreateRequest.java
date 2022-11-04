package kitchenpos.table.ui.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableGroupRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableGroupRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        final List<OrderTable> orderTables = this.orderTables
                .stream()
                .map(orderTableGroupRequest -> OrderTable.createByOnlyId(orderTableGroupRequest.getId()))
                .collect(Collectors.toList());
        return TableGroup.createSingleTables(orderTables);
    }
}
