package kitchenpos.ui.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupRequest {
    private final LocalDateTime createdDate;
    private final List<OrderTableRequest> orderTables;

    public TableGroupRequest(final LocalDateTime createdDate, final List<OrderTableRequest> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupRequest that = (TableGroupRequest) o;
        return Objects.equals(createdDate, that.createdDate)
                && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdDate, orderTables);
    }

    @Override
    public String toString() {
        return "TableGroupRequest{" +
                "createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}
