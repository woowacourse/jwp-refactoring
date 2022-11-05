package kitchenpos.order.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Icon;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

public class OrderResponse {
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemsResponse> orderLineItems;

    public OrderResponse(final Long id,
                         final Long orderTableId,
                         final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemsResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse of(final Order order,
                                   final List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                convertToOrderLineItemResponse(orderLineItems)
        );
    }

    private static List<OrderLineItemsResponse> convertToOrderLineItemResponse(final List<OrderLineItem> orderLineItems){
        return orderLineItems.stream()
                .map(OrderLineItemsResponse::new)
                .collect(Collectors.toList());
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

    public List<OrderLineItemsResponse> getOrderLineItems() {
        return orderLineItems;
    }
}
