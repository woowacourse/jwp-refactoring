package kitchenpos.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.tablegroup.TableGroup;

public class TableGroupResponse {

    private final long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(final long id,
                              final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.of(tableGroup.getOrderTables())
        );
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
