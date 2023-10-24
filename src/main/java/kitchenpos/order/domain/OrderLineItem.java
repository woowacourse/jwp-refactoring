package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Embedded
    private MenuSnapShot menuSnapShot;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menuSnapShot = MenuSnapShot.from(menu);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public MenuSnapShot getMenuSnapShot() {
        return menuSnapShot;
    }

    public long getQuantity() {
        return quantity;
    }
}
