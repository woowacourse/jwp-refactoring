package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Embedded;

public class OrderLineItem {

    @Id
    private final Long seq;
    private final Long menuId;
    private final long quantity;

    @Embedded.Nullable
    private final OrderMenu orderMenu;

    public OrderLineItem(final long quantity, final OrderMenu orderMenu) {
        this(null, orderMenu.getMenuId(), quantity, orderMenu);
    }

    @PersistenceCreator
    private OrderLineItem(final Long seq, final Long menuId, final long quantity, final OrderMenu orderMenu) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.orderMenu = orderMenu;
    }

    public OrderLineItem addOrderMenu(final OrderMenu orderMenu) {
        return new OrderLineItem(seq, orderMenu.getMenuId(), quantity, orderMenu);
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

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }
}
