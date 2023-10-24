package kitchenpos.domain;

public class OrderLineItem {
    private final Long seq;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    private OrderLineItem(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public OrderLineItem updateOrderId(Long orderId) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    public static final class OrderLineItemBuilder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        private OrderLineItemBuilder() {
        }

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, orderId, menuId, quantity);
        }
    }
}
