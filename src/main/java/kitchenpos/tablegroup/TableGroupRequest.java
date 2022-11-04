package kitchenpos.tablegroup;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.ui.OrderTableRequest;

public class TableGroupRequest {

    private final List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
        return TableGroup.of(toOrderTable(orderTables));
    }

    private List<OrderTable> toOrderTable(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList());
    }
}
