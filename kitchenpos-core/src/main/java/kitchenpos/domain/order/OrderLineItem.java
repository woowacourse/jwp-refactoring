package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(
            name = "ordered_menu_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_line_item_ordered_menu")
    )
    @OneToOne(fetch = FetchType.LAZY)
    private OrderedMenu orderedMenu;

    @Column(nullable = false)
    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(final OrderedMenu orderedMenu, final long quantity) {
        this(null, orderedMenu, quantity);
    }

    private OrderLineItem(final Order order, final OrderedMenu orderedMenu, final long quantity) {
        this(null, order, orderedMenu, quantity);
    }

    private OrderLineItem(final Long seq, final Order order, final OrderedMenu orderedMenu, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public Long seq() {
        return seq;
    }

    public Order order() {
        return order;
    }

    public OrderedMenu orderedMenu() {
        return orderedMenu;
    }

    public long quantity() {
        return quantity;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
