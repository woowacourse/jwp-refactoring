package kitchenpos.application.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                          List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderResponse from(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(order.getId(), order.getOrderTable().getId(), order.getOrderStatus(),
                order.getOrderedTime(), orderLineItemResponses);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
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
        private final Long order;
        private final Long menuId;
        private final Long quantity;

        private OrderLineItemResponse(Long seq, Long order, Long menuId, Long quantity) {
            this.seq = seq;
            this.order = order;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                    orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
        }

        public Long getSeq() {
            return seq;
        }

        public Long getOrder() {
            return order;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
