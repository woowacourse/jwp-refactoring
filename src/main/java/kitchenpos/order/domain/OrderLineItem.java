package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
        setOrder(order);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public static class Builder {

        private Long seq;
        private Order order;
        private Menu menu;
        private long quantity;

        public Builder seq(Long seq) {
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

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, order, menu, quantity);
        }
    }
}
