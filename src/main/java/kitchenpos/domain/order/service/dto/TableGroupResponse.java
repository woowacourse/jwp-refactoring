package kitchenpos.domain.order.service.dto;

import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {

    private final Long id;

    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse toDto(final TableGroup savedTableGroup) {
        final List<OrderTableResponse> orderTableResponses = savedTableGroup.getOrderTables().getOrderTables().stream()
                .map(OrderTableResponse::toDto)
                .collect(toList());
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(), orderTableResponses);
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
