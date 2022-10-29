package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<TableGroupOrderTableIdRequest> orderTables;

    public TableGroupCreateRequest(final List<TableGroupOrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupOrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
