package kitchenpos.menu.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Order;
import kitchenpos.menu.domain.OrderLineItem;

public class OrderResponse {

    private Long id;
    private String orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemResponse> orderLineItems;

    protected OrderResponse() {
    }

    public OrderResponse(final Long id, final String orderStatus, final LocalDateTime orderedTime,
                         final List<OrderLineItemResponse> orderLineItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static List<OrderResponse> listFrom(final List<OrderLineItem> orderLineItems) {
        Map<Order, List<OrderLineItem>> orderLineItemsPerOrder = orderLineItems.stream()
            .collect(Collectors.groupingBy(OrderLineItem::getOrder));

        return orderLineItemsPerOrder.keySet().stream()
            .map(order -> OrderResponse.from(order, orderLineItemsPerOrder.get(order)))
            .collect(Collectors.toList());
    }

    public static OrderResponse from(final Order order, final List<OrderLineItem> orderLineItems) {
        return new OrderResponse(
            order.getId(),
            order.getOrderStatus().name(),
            order.getOrderedTime(),
            OrderLineItemResponse.listFrom(orderLineItems)
        );
    }

    public Long getId() {
        return id;
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

        private Long seq;
        private Long orderId;
        private Long menuId;
        private Long quantity;

        protected OrderLineItemResponse() {
        }

        public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId,
                                     final Long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static List<OrderLineItemResponse> listFrom(
            final List<OrderLineItem> orderLineItems) {
            return orderLineItems.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
        }

        public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrder().getId(),
                orderLineItem.getOrderMenu().getMenuId(),
                orderLineItem.getQuantity().getValue()
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
