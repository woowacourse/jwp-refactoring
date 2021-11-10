package kitchenpos.order.ui.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public static TableGroupCreateRequest create(List<OrderTableRequest> orderTableRequests) {
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest();
        tableGroupCreateRequest.orderTables = orderTableRequests;
        return tableGroupCreateRequest;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toEntity() {
        final List<OrderTable> orderTables = this.orderTables.stream()
            .map(orderTableRequest -> OrderTable.createBySingleId(orderTableRequest.getId()))
            .collect(Collectors.toList());
        return TableGroup.createSingleTables(orderTables);
    }
}
