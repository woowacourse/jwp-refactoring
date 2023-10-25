package kitchenpos.tablegroup.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<TableGroupOrderTableRequest> orderTables = new ArrayList<>();

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<TableGroupOrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupOrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
