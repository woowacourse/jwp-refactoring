package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    private TableGroupResponse(final Long id,
                               final LocalDateTime createdDate,
                               final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup, final OrderTables orderTables) {
        final List<OrderTable> orderTablesEntities = orderTables.getValues();
        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.from(orderTablesEntities)
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
