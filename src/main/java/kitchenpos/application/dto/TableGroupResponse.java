package kitchenpos.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup tableGroup) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = toOrderTableResponse(tableGroup.getOrderTables());
    }

    private List<OrderTableResponse> toOrderTableResponse(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.stream()
                .map(orderTableResponse -> new OrderTable(
                        orderTableResponse.getId(),
                        orderTableResponse.getTableGroupId(),
                        orderTableResponse.getNumberOfGuests(),
                        orderTableResponse.isEmpty()
                ))
                .collect(Collectors.toList());
    }
}
