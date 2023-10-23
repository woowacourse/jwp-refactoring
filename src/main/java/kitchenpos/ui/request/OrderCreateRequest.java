package kitchenpos.ui.request;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItem> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = "COOKING";
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(orderTableId, null, orderLineItems);
    }

    public OrderCreateRequest() {
    }

    public Order toOrder() {
        return new Order(orderTableId, orderStatus, orderedTime, orderLineItems);
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
