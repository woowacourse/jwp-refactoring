package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private OrderedMenu orderedMenu;

    @Column
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final OrderedMenu orderedMenu, final long quantity) {
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public OrderLineItem(final Order order, final OrderedMenu orderedMenu, final long quantity) {
        this.order = order;
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    private OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.orderedMenu = builder.orderedMenu;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class Builder {
        private Long seq;
        private Order order;
        private OrderedMenu orderedMenu;
        private long quantity;

        public Builder seq(final Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder order(final Order order) {
            this.order = order;
            return this;
        }

        public Builder orderedMenu(final OrderedMenu orderedMenu) {
            this.orderedMenu = orderedMenu;
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
