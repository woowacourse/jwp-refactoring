package kitchenpos.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupCreateRequest {
    private final LocalDateTime createdDate;
    private final List<Long> orderTableIdss;

    public TableGroupCreateRequest(LocalDateTime createdDate, List<Long> orderTableIdss) {
        this.createdDate = createdDate;
        this.orderTableIdss = orderTableIdss;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Long> getOrderTableIdss() {
        return orderTableIdss;
    }
}
