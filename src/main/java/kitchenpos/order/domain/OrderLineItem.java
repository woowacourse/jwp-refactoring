package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    public OrderLineItem() {
    }

    public OrderLineItem(Long quantity, Menu menu) {
        this.quantity = quantity;
        this.menu = menu;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

}
