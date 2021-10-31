package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private Long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Builder builder) {
        this.seq = builder.seq;
        this.order = builder.order;
        this.menu = builder.menu;
        this.quantity = builder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void registerOrder(Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Menu getMenu() {
        return menu;
    }

    public static class Builder {
        private Long seq;
        private Order order;
        private Menu menu;
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

        public Builder menu(Menu menu) {
            this.menu = menu;
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
