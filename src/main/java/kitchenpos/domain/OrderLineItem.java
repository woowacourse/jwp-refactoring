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

    private Long menuId;

    @Embedded
    private MenuSnapShot menuSnapShot;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final MenuSnapShot menuSnapShot, final long quantity) {
        this.menuId = menuId;
        this.menuSnapShot = menuSnapShot;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
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
