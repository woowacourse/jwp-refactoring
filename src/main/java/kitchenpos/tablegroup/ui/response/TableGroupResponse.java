package kitchenpos.tablegroup.ui.response;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.ui.response.OrderTableResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;

    private TableGroupResponse(final Long id,
                               final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate()
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroupResponse that = (TableGroupResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }

    @Override
    public String toString() {
        return "TableGroupResponse{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                '}';
    }
}
