package kitchenpos.dto.request;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private final List<OrderTableId> orderTables;

    @ConstructorProperties({"orderTables"})
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
