package kitchenpos.dto.response;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    private OrderLineItemResponse(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return OrderLineItemResponse.builder()
                .seq(orderLineItem.getSeq())
                .menuId(orderLineItem.getMenuId())
                .quantity(orderLineItem.getQuantity())
                .build();
    }

    public static OrderLineItemResponseBuilder builder() {
        return new OrderLineItemResponseBuilder();
    }

    public static final class OrderLineItemResponseBuilder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        private OrderLineItemResponseBuilder() {
        }

        public OrderLineItemResponseBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemResponseBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemResponseBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemResponseBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemResponse build() {
            return new OrderLineItemResponse(seq, orderId, menuId, quantity);
        }
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
