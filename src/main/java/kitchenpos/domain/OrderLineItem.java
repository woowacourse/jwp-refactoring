package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long orderId;

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(
            final Long menuId,
            final long quantity
    ) {
        this(null, null, menuId, quantity);
    }

    public OrderLineItem(
            final Long orderId,
            final Long menuId,
            final long quantity
    ) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(
            final Long seq,
            final Long orderId,
            final Long menuId,
            final long quantity
    ) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }


    public long getQuantity() {
        return quantity;
    }
}
