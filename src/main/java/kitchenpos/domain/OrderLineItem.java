package kitchenpos.domain;

import kitchenpos.domain.vo.Quantity;

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

    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    protected OrderLineItem(final Order order, final Menu menu, final Quantity quantity) {
        this(null, order, menu, quantity);
    }

    protected OrderLineItem(final Long seq, final Order order, final Menu menu, final Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem withoutOrder(final Menu menu, final Quantity quantity) {
        return new OrderLineItem(null, menu, quantity);
    }

    public void assignOrder(final Order requestOrder) {
        this.order = requestOrder;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setOrderId(final Long orderId) {
        this.order.setId(orderId);
    }
}
