package kitchenpos.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.order_lineitem.OrderLineItem;
import kitchenpos.domain.order.order_lineitem.OrderLineItems;

public class OrderResponse {

    private final Long id;
    private final Long orderTableId;
    private final String orderStatus;
    private final LocalDateTime orderedTime;
    private final List<OrderLineItemResponse> orderLineItems;

    public OrderResponse(
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
        final Long id = order.getId();
        return new OrderResponse(
                id,
                order.getOrderTable().getId(),
                order.getOrderStatus().name(),
                order.getOrderedTime(),
                createOrderLineItemResponse(order.getOrderLineItems(), id)
        );
    }

    private static List<OrderLineItemResponse> createOrderLineItemResponse(
            final OrderLineItems orderLineItems,
            final Long id
    ) {
        return orderLineItems.getOrderLineItems()
                .stream()
                .map(it -> OrderLineItemResponse.from(it, id))
                .collect(Collectors.toUnmodifiableList());
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
        private final long quantity;

        public OrderLineItemResponse(
                final Long seq,
                final Long orderId,
                final Long menuId,
                final long quantity
        ) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderLineItemResponse from(final OrderLineItem orderlIneItem, final Long orderId) {
            return new OrderLineItemResponse(
                    orderlIneItem.getSeq(),
                    orderId,
                    orderlIneItem.getMenu().getId(),
                    orderlIneItem.getQuantity()
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

        public long getQuantity() {
            return quantity;
        }
    }
}
