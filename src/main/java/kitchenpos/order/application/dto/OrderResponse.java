package kitchenpos.order.application.dto;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(
            final Long id,
            final Long orderTableId,
            final String orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItemResponse> orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(final Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderTableId(),
                order.getOrderStatus(),
                order.getOrderedTime(),
                order.getOrderLineItems().stream()
                        .map(orderLineItem -> OrderLineItemResponse.from(orderLineItem, order.getId()))
                        .collect(Collectors.toList())
        );
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

    public List<OrderLineItemResponse> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemResponse {

        private final Long seq;
        private final Long orderId;
        private final Long menuId;
        private final Long quantity;

        private OrderLineItemResponse(
                final Long seq,
                final Long orderId,
                final Long menuId,
                final Long quantity
        ) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(final OrderLineItem orderLineItem, final Long orderId) {
            return new OrderLineItemResponse(
                    orderLineItem.getSeq(),
                    orderId,
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
        }

        public Long getSeq() {
            return seq;
        }

        public Long getOrderId() {
            return orderId;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
