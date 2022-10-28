package kitchenpos.table.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdTime;
    private final List<OrderTableResponse> orderTables;

    private TableGroupResponse(Long id, LocalDateTime createdTime, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdTime = createdTime;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse toResponse(TableGroup tableGroup, OrderTables orderTables) {
        List<OrderTableResponse> orderTableResponses = orderTables.getOrderTables().stream()
            .map(OrderTableResponse::toResponse)
            .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableResponses);
    }

    public Long getId() {
        return id;
    }
}
