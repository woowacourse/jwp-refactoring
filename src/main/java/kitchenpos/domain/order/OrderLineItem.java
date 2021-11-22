package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(OrderLineItemBuilder orderLineItemBuilder) {
        this.seq = orderLineItemBuilder.seq;
        this.order = orderLineItemBuilder.order;
        this.menu = orderLineItemBuilder.menu;
        this.quantity = orderLineItemBuilder.quantity;
    }

    public void changeOrder(Order order) {
        this.order = order;
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

    public Long getQuantity() {
        return quantity;
    }

    public static class OrderLineItemBuilder {

        private Long seq;
        private Order order;
        private Menu menu;
        private Long quantity;

        public OrderLineItemBuilder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder setOrder(Order order) {
            this.order = order;
            return this;
        }

        public OrderLineItemBuilder setMenu(Menu menu) {
            this.menu = menu;
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
