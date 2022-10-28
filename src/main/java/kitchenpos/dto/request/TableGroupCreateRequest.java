package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
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
