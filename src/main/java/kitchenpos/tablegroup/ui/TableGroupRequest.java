package kitchenpos.tablegroup.ui;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.ui.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;

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
