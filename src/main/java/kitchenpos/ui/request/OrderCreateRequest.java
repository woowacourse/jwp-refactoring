package kitchenpos.ui.request;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest(final Long orderTableId, final LocalDateTime orderedTime, final List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = "COOKING";
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public OrderCreateRequest(final Long orderTableId, final List<OrderLineItemCreateRequest> orderLineItems) {
        this(orderTableId, null, orderLineItems);
    }

    public OrderCreateRequest() {
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

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
