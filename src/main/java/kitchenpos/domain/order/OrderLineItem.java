package kitchenpos.domain.order;

import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders"))
    private Order order;
    @OneToOne(optional = false, cascade = PERSIST)
    @JoinColumn(name = "order_menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_to_order_menu"))
    private OrderMenu orderMenu;
    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Menu menu, final long quantity) {
        this.orderMenu = new OrderMenu(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getQuantityByProduct()
        );
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getMenuId() {
        return orderMenu.getMenuId();
    }
}
