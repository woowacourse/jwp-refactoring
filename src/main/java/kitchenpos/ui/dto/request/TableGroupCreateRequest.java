package kitchenpos.ui.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<Long> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupCreateRequest(final TableGroupCreateIds orderTableIds) {
        this.orderTables = orderTableIds.getOrderTables()
                .stream()
                .map(TableGroupCreateId::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
