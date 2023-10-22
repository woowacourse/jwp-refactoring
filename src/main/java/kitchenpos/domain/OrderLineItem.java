package kitchenpos.domain;

import javax.persistence.Entity;
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

    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final long quantity) {
        this.seq = seq;
        this.quantity = quantity;
    }

    public static OrderLineItem forSave(final long quantity) {
        return new OrderLineItem(null, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }
}
