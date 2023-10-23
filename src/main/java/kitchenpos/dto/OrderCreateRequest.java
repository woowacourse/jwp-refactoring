package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderLineItem;

public class OrderCreateRequest {

    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItem> orderLineItems;

    public OrderCreateRequest(
            final Long orderTableId,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
