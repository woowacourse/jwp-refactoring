package kitchenpos.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreateRequest {
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineRequest> orderLines;

    public OrderCreateRequest(Long orderTableId, String orderStatus, LocalDateTime orderedTime,
                              List<OrderLineRequest> orderLines) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLines = orderLines;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineRequest> getOrderLines() {
        return orderLines;
    }
}
