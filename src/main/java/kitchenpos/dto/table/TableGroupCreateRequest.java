package kitchenpos.dto.table;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<SingleOrderTableCreateRequest> orderTables;

    private TableGroupCreateRequest(final List<SingleOrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroupCreateRequest from(final List<SingleOrderTableCreateRequest> orderTables) {
        return new TableGroupCreateRequest(orderTables);
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
