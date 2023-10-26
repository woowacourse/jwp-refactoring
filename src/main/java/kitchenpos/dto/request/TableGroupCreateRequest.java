package kitchenpos.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateRequest {

    private final LocalDateTime createdDate;
    private final List<Long> orderTableIds;

    public TableGroupCreateRequest(LocalDateTime createdDate, List<Long> orderTableIds) {
        this.createdDate = createdDate;
        this.orderTableIds = orderTableIds;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
