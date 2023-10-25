package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private MenuSnapShot menuSnapShot;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final MenuSnapShot menuSnapShot, final long quantity) {
        this.menuSnapShot = menuSnapShot;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuSnapShot getMenuSnapShot() {
        return menuSnapShot;
    }
}
