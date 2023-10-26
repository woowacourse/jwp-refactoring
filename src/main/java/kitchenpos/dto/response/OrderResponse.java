package kitchenpos.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.vo.OrderStatus;

public class OrderResponse {

    private final long id;
    private final long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    private OrderResponse(final long id,
                          long orderTableId,
                          final OrderStatus orderStatus,
                          final LocalDateTime orderedTime,
                          final List<OrderLineItemResponse> orderLineItems) {
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
                OrderLineItemResponse.of(order.getOrderLineItems())
        );
    }

    public static List<OrderResponse> of(final List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public long getOrderTableId() {
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

        private final long seq;
        private final long menuId;
        private final long quantity;

        private OrderLineItemResponse(final long seq,
                                      final long menuId,
                                      final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
            return new OrderLineItemResponse(
                    orderLineItem.getSeq(),
                    orderLineItem.getMenuId(),
                    orderLineItem.getQuantity()
            );
        }

        public static List<OrderLineItemResponse> of(final List<OrderLineItem> orderLineItems) {
            return orderLineItems.stream()
                    .map(OrderLineItemResponse::from)
                    .collect(Collectors.toList());
        }

        public long getSeq() {
            return seq;
        }

        public long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
