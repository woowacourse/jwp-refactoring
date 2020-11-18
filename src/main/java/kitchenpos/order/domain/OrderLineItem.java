package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long quantity;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Menu menu;

    public OrderLineItem() {
    }

    public OrderLineItem(Long quantity, Order order, Menu menu) {
        this.quantity = quantity;
        this.order = order;
        this.menu = menu;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
