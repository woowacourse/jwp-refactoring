package kitchenpos.ui.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class TableGroupRequest {

    @NotEmpty
    @Valid
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupRequest that = (TableGroupRequest) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
            "orderTables=" + orderTables +
            '}';
    }
}
