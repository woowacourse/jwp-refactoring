package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class OrderLineItem {

    @Id
    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, menuId, quantity);
    }

    @PersistenceCreator
    private OrderLineItem(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
