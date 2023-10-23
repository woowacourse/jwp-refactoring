package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableGroupCreateRequest {

    private final LocalDateTime createdDate;
    private final List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest(final LocalDateTime createdDate, final List<OrderTableRequest> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
