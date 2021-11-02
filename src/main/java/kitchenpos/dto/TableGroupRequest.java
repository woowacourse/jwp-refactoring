package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
