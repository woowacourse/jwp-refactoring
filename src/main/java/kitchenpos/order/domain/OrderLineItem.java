package kitchenpos.order.domain;

import kitchenpos.menu.domain.vo.Quantity;

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

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "menu_history_id", nullable = true)
    private Long menuHistoryId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId,
                         final Quantity quantity
    ) {
        this(null, menuId, null, quantity);
    }

    protected OrderLineItem(final Long seq,
                            final Long menuId,
                            final Long menuHistoryId,
                            final Quantity quantity
    ) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuHistoryId = menuHistoryId;
        this.quantity = quantity;
    }

    public static OrderLineItem withoutOrder(final Long menuId, final Quantity quantity) {
        return new OrderLineItem(menuId,  quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getMenuHistoryId() {
        return menuHistoryId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
