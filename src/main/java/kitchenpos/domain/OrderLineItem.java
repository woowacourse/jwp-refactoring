package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @Column(name = "seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_orders"))
    private Order order;
    @ManyToOne(optional = false)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_line_item_to_menu"))
    private Menu menu;
    @Column(nullable = false)
    private long quantity;

    public OrderLineItem(final Order order, final Menu menu, final long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        order.getOrderLineItems().add(this);
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    protected OrderLineItem() {
    }
}
