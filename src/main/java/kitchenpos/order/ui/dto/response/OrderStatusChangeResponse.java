package kitchenpos.order.ui.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderStatusChangeResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemChangeResponse> orderLineItems;

    public OrderStatusChangeResponse() {
    }

    private OrderStatusChangeResponse(final Long id, final Long orderTableId, final String orderStatus,
                                      final LocalDateTime orderedTime,
                                      final List<OrderLineItemChangeResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderStatusChangeResponse of(final Order order, final List<OrderLineItem> orderLineItems) {
        return new OrderStatusChangeResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus().name(),
                order.getOrderedTime(), OrderLineItemChangeResponse.from(orderLineItems));
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

    public List<OrderLineItemChangeResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
