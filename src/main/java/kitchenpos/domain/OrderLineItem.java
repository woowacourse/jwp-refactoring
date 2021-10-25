package kitchenpos.domain;

public class OrderLineItem {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.orderId = builder.orderId;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        private Builder() {
        }

        public Builder of(OrderLineItem orderLineItem) {
            this.seq = orderLineItem.seq;
            this.orderId = orderLineItem.orderId;
            this.menuId = orderLineItem.menuId;
            this.quantity = orderLineItem.quantity;
            return this;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }

    }

    public Long getSeq() {
        return seq;
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

    public long getQuantity() {
        return quantity;
    }

}
