package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public final class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<Long> orderLineItemIds;

    public OrderResponse(
            final Long id,
            final Long orderTableId,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<Long> orderLineItemIds
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItemIds = orderLineItemIds;
    }

    public Long getId() {
        return id;
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

    public List<Long> getOrderLineItemIds() {
        return orderLineItemIds;
    }
}
