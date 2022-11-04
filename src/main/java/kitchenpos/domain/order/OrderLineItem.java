package kitchenpos.domain.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private OrderedMenu orderedMenu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final OrderedMenu orderedMenu, final long quantity) {
        this(null, orderedMenu, quantity);
    }

    private OrderLineItem(final Long seq, final OrderedMenu orderedMenu, final long quantity) {
        this.seq = seq;
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
