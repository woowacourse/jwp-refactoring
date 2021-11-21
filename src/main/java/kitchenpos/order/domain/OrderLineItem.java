package kitchenpos.order.domain;

import javax.persistence.Column;
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
    private Long id;
    @Column
    private Long orderId;
    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private Long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(Builder builder) {
        this.id = builder.id;
        this.orderId = builder.orderId;
        this.menuId = builder.menuId;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long orderId;
        private Long menuId;
        private long quantity;

        private Builder() {
        }

        public Builder of(OrderLineItem orderLineItem) {
            this.id = orderLineItem.id;
            this.orderId = orderLineItem.orderId;
            this.menuId = orderLineItem.menuId;
            this.quantity = orderLineItem.quantity;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder order(Long orderId) {
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

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void updateOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

}
