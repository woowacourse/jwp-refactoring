package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOrderTables(), that.getOrderTables());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
