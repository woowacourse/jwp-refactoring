package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Menu menu;

    private long quantity;

    public OrderLineItem() {}

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
