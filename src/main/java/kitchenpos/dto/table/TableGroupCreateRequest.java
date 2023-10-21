package kitchenpos.dto.table;

import java.util.List;

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
}
