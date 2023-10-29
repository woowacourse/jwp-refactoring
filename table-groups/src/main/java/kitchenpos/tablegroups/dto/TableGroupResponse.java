package kitchenpos.tablegroups.dto;


import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroups.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(final TableGroup tableGroup) {
        final List<OrderTableResponse> orderTables = tableGroup.getOrderTables().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTables);
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
