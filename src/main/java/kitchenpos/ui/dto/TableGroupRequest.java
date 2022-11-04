package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

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
