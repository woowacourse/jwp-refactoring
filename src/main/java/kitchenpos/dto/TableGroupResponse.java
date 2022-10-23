package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        List<OrderTableResponse> orderTableResponses = tableGroup.getOrderTables()
            .stream()
            .map(OrderTableResponse::from)
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
