package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

public class OrderLineItem {
    @Id
    private Long seq;
    private AggregateReference<Menu, Long> menuId;
    private long quantity;

    public OrderLineItem(final Long seq, final Long menuId, final long quantity) {
        this.seq = seq;
        this.menuId = AggregateReference.to(menuId);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
