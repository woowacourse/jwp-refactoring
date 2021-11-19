package kitchenpos.domain.orderlineitem;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.orderedmenu.OrderedMenu;
import kitchenpos.domain.quantity.Quantity;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_orders"))
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_ordered_menu"))
    private OrderedMenu orderedMenu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(OrderedMenu orderedMenu, Quantity quantity) {
        this(null, null, orderedMenu, quantity);
    }

    public OrderLineItem(Order order, OrderedMenu orderedMenu, Long quantityValue) {
        this(null, order, orderedMenu, new Quantity(quantityValue));
    }

    public OrderLineItem(Long seq, Order order, OrderedMenu orderedMenu, Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.orderedMenu = orderedMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return orderedMenu.getMenuId();
    }

    public Long getOrderedMenuId() {
        return orderedMenu.getId();
    }

    public Long getQuantityValue() {
        return quantity.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
