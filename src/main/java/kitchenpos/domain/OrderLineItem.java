package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

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

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuSnapShot getMenuSnapShot() {
        return menuSnapShot;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
