package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<TableGroupOrderTableRequest> orderTables;

    public TableGroupCreateRequest(final List<TableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
