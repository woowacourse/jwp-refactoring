package kitchenpos.tablegroup.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.service.dto.TableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private final long id;
    private final LocalDateTime createdDate;
    private final List<TableResponse> orderTables;

    private TableGroupResponse(final long id, final LocalDateTime createdDate, final List<TableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                TableResponse.from(tableGroup.getOrderTables())
        );
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<TableResponse> getOrderTables() {
        return orderTables;
    }
}
