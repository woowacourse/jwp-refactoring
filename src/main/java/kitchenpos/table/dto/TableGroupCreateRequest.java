package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        final List<OrderTable> orderTables = this.orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList());

        return new TableGroup(null, null, orderTables);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
