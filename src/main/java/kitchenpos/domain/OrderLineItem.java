package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @Column
    private Long menuId;

    @Column
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
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

    public static class Builder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public Builder seq(final Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder orderId(final Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder menuId(final Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(this);
        }
    }
}
