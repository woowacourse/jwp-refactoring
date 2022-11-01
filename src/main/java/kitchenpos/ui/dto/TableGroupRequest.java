package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    private TableGroupRequest() {
    }

    public TableGroup toTableGroup() {
        return new TableGroup(LocalDateTime.now(), toOrderTable(orderTables));
    }

    private List<OrderTable> toOrderTable(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::toOrderTable)
                .collect(Collectors.toList());
    }
}
