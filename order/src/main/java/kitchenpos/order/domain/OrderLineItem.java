package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long quantity;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(
        nullable = false, updatable = false
    )
    private MenuSnapshot menuSnapshot;

    protected OrderLineItem() {
    }

    public OrderLineItem(
        final MenuSnapshot menuSnapshot,
        final long quantity
    ) {
        this.menuSnapshot = menuSnapshot;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuSnapshot getMenuSnapshot() {
        return menuSnapshot;
    }
}
