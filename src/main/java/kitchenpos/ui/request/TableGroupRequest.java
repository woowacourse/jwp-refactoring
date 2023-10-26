package kitchenpos.ui.request;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {
    private final List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupRequest that = (TableGroupRequest) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
                ", orderTables=" + orderTables +
                '}';
    }
}
