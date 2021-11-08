package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long menuId;

    private Long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public static class Builder {
        private Long seq;
        private Order order;
        private Long menuId;
        private Long quantity;

        public Builder() {
        }

        public Builder id(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder order(Order order) {
            this.order = order;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }
    }
}
