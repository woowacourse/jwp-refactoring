package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.BaseEntity;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem extends BaseEntity {

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

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    protected OrderLineItem() {
    }
}
