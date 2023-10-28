package kitchenpos.order;

import kitchenpos.menu.Menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long seq;

    @Column
    private long quantity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order"))
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
    private Menu menu;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final long quantity, final Order order, final Menu menu) {
        this.seq = seq;
        this.quantity = quantity;
        this.order = order;
        this.menu = menu;
    }

    public OrderLineItem(final long quantity, final Order order, final Menu menu) {
        this(null, quantity, order, menu);
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }
}
