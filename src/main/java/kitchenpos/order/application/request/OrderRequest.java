package kitchenpos.order.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.List;

public class OrderRequest {

    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemRequest> orderLineItems;

    @JsonCreator
    public OrderRequest(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                        final List<OrderLineItemRequest> orderLineItems) {
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

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
