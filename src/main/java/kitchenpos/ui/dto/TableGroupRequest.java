package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final Long id, final LocalDateTime createdDate,
                             final List<OrderTableRequest> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup() {
        //:todo LocalDateTime.now()?
        return new TableGroup(id, createdDate,
                orderTables.stream()
                        .map(OrderTableRequest::toOrderTable)
                        .collect(Collectors.toList()));
    }
}
