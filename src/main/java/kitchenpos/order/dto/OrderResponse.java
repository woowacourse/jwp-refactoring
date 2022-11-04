package kitchenpos.order.dto;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse() {
    }

    public OrderResponse(final Order order) {
        this.id = order.getId();
        this.orderTableId = order.getOrderTableId();
        this.orderStatus = order.getOrderStatus();
        this.orderedTime = order.getOrderedTime();
        this.orderLineItems = order.getOrderLineItems()
                .stream()
                .map(OrderLineItemResponse::new)
                .collect(toList());
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
