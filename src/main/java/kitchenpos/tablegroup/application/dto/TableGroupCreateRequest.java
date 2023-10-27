package kitchenpos.tablegroup.application.dto;

import kitchenpos.ordertable.application.dto.OrderTableId;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableId> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> extractIds() {
        return orderTables.stream()
                .map(OrderTableId::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
