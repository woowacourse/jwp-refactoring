package kitchenpos.domain;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@AttributeOverride(name = "id", column = @Column(name = "order_line_item_id"))
@Entity
public class OrderLineItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_ORDER"))
    private Order order;

    @OneToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "FK_ORDER_LINE_ITEM_MENU"))
    private Menu menu;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Menu menu, long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
