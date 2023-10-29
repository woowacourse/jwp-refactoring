package kitchenpos.application.dto;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderResponse {

    private Long id;

    private String orderStatus;

    private List<OrderLineItem> orderLineItems;

    public OrderResponse() {
    }

    public OrderResponse(final Long id, final String orderStatus, final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus().name(), order.getOrderLineItems());
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
