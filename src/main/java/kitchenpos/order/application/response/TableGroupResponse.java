package kitchenpos.order.application.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public static TableGroupResponse from(TableGroup tableGroup) {
        final TableGroupResponse tableGroupResponse = new TableGroupResponse();
        tableGroupResponse.id = tableGroup.getId();
        tableGroupResponse.createdDate = tableGroup.getCreatedDate();
        tableGroupResponse.orderTables = OrderTableResponse.fromList(tableGroup.getOrderTables());
        return tableGroupResponse;
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
