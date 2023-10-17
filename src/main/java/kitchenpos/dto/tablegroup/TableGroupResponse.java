package kitchenpos.dto.tablegroup;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.OrderTablesResponse;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final OrderTablesResponse orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final OrderTablesResponse orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                OrderTablesResponse.from(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTablesResponse getOrderTables() {
        return orderTables;
    }
}
