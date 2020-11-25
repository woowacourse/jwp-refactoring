package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        return new TableGroupResponse(
            tableGroup.getId(),
            tableGroup.getCreatedDate(),
            orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList())
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
