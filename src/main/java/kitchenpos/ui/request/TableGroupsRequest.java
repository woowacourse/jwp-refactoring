package kitchenpos.ui.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupsRequest {
    private final List<TableGroupIdRequest> orderTables;

    public TableGroupsRequest(final List<TableGroupIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables
                .stream().map(TableGroupIdRequest::getId)
                .collect(Collectors.toList());
    }
}
