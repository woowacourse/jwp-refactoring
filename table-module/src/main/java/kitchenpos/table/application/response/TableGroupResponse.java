package kitchenpos.table.application.response;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(final long id,
                              final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.from(tableGroup.getOrderTables().getOrderTableItems())
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
