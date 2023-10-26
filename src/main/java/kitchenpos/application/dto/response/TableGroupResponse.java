package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate,
                               final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.from(tableGroup.getOrderTablesValue())
        );
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
