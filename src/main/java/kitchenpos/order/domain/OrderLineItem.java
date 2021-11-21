package kitchenpos.order.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @OneToOne
    private OrderMenu orderMenu;

    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.orderMenu = builder.orderMenu;
        this.quantity = builder.quantity;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderMenuId() {
        return orderMenu.getId();
    }

    public String getOrderMenuName() {
        return orderMenu.getName();
    }

    public BigDecimal getOrderMenuPrice() {
        return orderMenu.getPrice();
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
        private OrderMenu orderMenu;
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

        public Builder orderMenu(OrderMenu orderMenu) {
            this.orderMenu = orderMenu;
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
