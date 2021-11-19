package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupRequest {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest(Long id,
                             LocalDateTime createdDate,
                             List<OrderTableRequest> orderTableRequests
    ) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableRequests = orderTableRequests;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public TableGroup toTableGroup() {
        final List<OrderTable> orderTables = orderTableRequests.stream()
            .map(OrderTableRequest::toOrderTable)
            .collect(Collectors.toList());
        return new TableGroup(id, createdDate, orderTables);
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
