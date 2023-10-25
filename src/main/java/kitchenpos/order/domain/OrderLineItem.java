package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.vo.Quantity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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

    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    protected OrderLineItem(final Menu menu, final Quantity quantity) {
        this(null, menu, quantity);
    }

    protected OrderLineItem(final Long seq, final Menu menu, final Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem withoutOrder(final Menu menu, final Quantity quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
