package kitchenpos.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;

public class OrderCreateResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemCreateResponse> orderLineItems;

    public OrderCreateResponse() {
    }

    private OrderCreateResponse(final Long id, final Long orderTableId, final String orderStatus,
                                final LocalDateTime orderedTime,
                                final List<OrderLineItemCreateResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderCreateResponse of(final Order order, final List<OrderLineItem> orderLineItems) {
        return new OrderCreateResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), OrderLineItemCreateResponse.from(orderLineItems));
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

    public List<OrderLineItemCreateResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
