package kitchenpos.application.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(final Long id, final Long orderTableId, final String orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public static OrderResponse of(final Order order, final List<OrderLineItem> orderLineItems) {
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemResponses);
    }

    public static class OrderLineItemResponse {

        private final Long seq;
        private final Long orderId;
        private String name;
        private BigDecimal price;
        private final long quantity;

        public OrderLineItemResponse(final Long seq, final Long orderId, final String name, final BigDecimal price,
                                     final long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrderId(),
                    orderLineItem.getName(), orderLineItem.getPrice(), orderLineItem.getQuantity());
        }
    }
}
