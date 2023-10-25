package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;

public class OrderLineItem {
    @Id
    private Long seq;
    @Column("MENU_ID")
    private AggregateReference<Menu, Long> menu;
    private long quantity;

    public OrderLineItem(Long menuId, long quantity) {
        this(null, menuId, quantity);
    }

    @PersistenceCreator
    public OrderLineItem(Long seq, Long menu, long quantity) {
        this.seq = seq;
        this.menu = AggregateReference.to(menu);
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
