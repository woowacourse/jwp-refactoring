package kitchenpos.domain;

public class OrderLineItem {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(OrderLineItemBuilder orderLineItemBuilder) {
        this.seq = orderLineItemBuilder.seq;
        this.orderId = orderLineItemBuilder.orderId;
        this.menuId = orderLineItemBuilder.menuId;
        this.quantity = orderLineItemBuilder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public static class OrderLineItemBuilder {

        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItemBuilder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder setOrderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemBuilder setMenuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder setQuantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }
    }
}
