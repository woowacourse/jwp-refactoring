package kitchenpos.order.domain;

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

    @Embedded
    private OrderedItem orderedItem;

    @Column
    private long quantity;

    protected OrderLineItem() {}

    public OrderLineItem(final OrderedItem orderedItem, final long quantity) {
        this.orderedItem = orderedItem;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return orderedItem.getMenuId();
    }

    public long getQuantity() {
        return quantity;
    }
}
