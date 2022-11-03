package kitchenpos.ui.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

public class OrderResponse {

    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderInnerLineItem> orderLineItems;

    private OrderResponse() {
    }

    private OrderResponse(final Long id,
                         final Long orderTableId,
                         final OrderStatus orderStatus,
                         final LocalDateTime orderedTime,
                         final List<OrderInnerLineItem> orderLineItems) {
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
                mapToInnerLineItems(order.getOrderLineItems())
        );
    }

    private static List<OrderInnerLineItem> mapToInnerLineItems(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderInnerLineItem::from)
                .collect(Collectors.toList());
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

    public List<OrderInnerLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderInnerLineItem {

        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        private OrderInnerLineItem() {
        }

        public OrderInnerLineItem(final Long seq,
                                  final Long orderId,
                                  final Long menuId,
                                  final long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public static OrderInnerLineItem from(final OrderLineItem orderLineItem) {
            return new OrderInnerLineItem(
                    orderLineItem.getSeq(),
                    orderLineItem.getOrderId(),
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

        public long getQuantity() {
            return quantity;
        }
    }
}
