package kitchenpos.tablegroup.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.application.dto.OrderTableResponse;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse(final Long id, final LocalDateTime createdDate,
                              final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());

        return new TableGroupResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                orderTableResponses
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
