package kitchenpos.domain.order;

import kitchenpos.domain.BaseEntity;
import kitchenpos.domain.menu.Menu;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@AttributeOverride(name = "id", column = @Column(name = "ORDER_LINE_ITEM_ID"))
@Entity
public class OrderLineItem extends BaseEntity {
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu) {
        this.menu = menu;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public Order getOrder() {
        return order;
    }
}
