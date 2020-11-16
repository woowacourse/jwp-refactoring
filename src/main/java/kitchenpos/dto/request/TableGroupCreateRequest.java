package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TableGroupCreateRequest {
    private final List<OrderTableId> orderTables;

    @JsonCreator
    public TableGroupCreateRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(OrderTableId::getId)
            .collect(Collectors.toList());
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
