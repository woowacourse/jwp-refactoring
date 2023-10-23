package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<SingleOrderTableCreateRequest> orderTables;

    public TableGroupCreateRequest(final List<SingleOrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<SingleOrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(SingleOrderTableCreateRequest::getId)
                .collect(Collectors.toList());
    }
}
