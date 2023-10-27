package kitchenpos.order.domain.model;

import javax.persistence.Column;
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

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long quantity;

    @Embedded
    private MenuSnapShot menuSnapShot;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Long quantity, MenuSnapShot menuSnapShot) {
        this(null, menuId, quantity, menuSnapShot);
    }

    public OrderLineItem(Long seq, Long menuId, Long quantity, MenuSnapShot menuSnapShot) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuSnapShot = menuSnapShot;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuSnapShot getMenuSnapShot() {
        return menuSnapShot;
    }
}
