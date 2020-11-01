package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupRequest {

    private List<TableOfTableGroupRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTables = orderTableIds.stream()
            .map(TableOfTableGroupRequest::new)
            .collect(Collectors.toList());
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
            .map(TableOfTableGroupRequest::getId)
            .collect(Collectors.toList());
    }

    public List<TableOfTableGroupRequest> getOrderTables() {
        return orderTables;
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
            "orderTables=" + orderTables +
            '}';
    }
}
