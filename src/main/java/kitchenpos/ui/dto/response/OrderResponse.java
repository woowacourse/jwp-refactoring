package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;

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

    public static OrderResponse from(final Order order) {
        final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                .stream()
                .map(it -> OrderLineItemResponse.of(it, order.getId()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderTableId(), order.getOrderStatus(), order.getOrderedTime(),
                orderLineItemResponses);
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

    private static class OrderLineItemResponse {

        private final Long seq;
        private final Long orderId;
        private final Long menuId;
        private final long quantity;
        private final String menuName;
        private final BigDecimal menuPrice;

        public OrderLineItemResponse(final Long seq, final Long orderId, final Long menuId, final long quantity,
                                     final String menuName, final BigDecimal menuPrice) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
            this.menuName = menuName;
            this.menuPrice = menuPrice;
        }

        public static OrderLineItemResponse of(final OrderLineItem orderLineItem, final Long orderId) {
            final OrderMenu orderMenu = orderLineItem.getOrderMenu();
            return new OrderLineItemResponse(
                    orderLineItem.getSeq(),
                    orderId,
                    orderMenu.getMenuId(),
                    orderLineItem.getQuantity(),
                    orderMenu.getMenuName(),
                    orderMenu.getMenuPrice()
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

        public String getMenuName() {
            return menuName;
        }

        public BigDecimal getMenuPrice() {
            return menuPrice;
        }
    }
}
