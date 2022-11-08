package kitchenpos.ordertable.ui.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<TableGroupOrderTableIdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableGroupOrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> mapToOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupOrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    public List<TableGroupOrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
