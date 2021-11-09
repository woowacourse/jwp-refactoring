package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.Menu;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private long quantity;

    protected OrderLineItem() {
    }

    public static OrderLineItem create(long id, long orderId, long menuId, long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.seq = id;
        orderLineItem.order = Order.createSingleId(orderId);
        orderLineItem.menu = Menu.createSingleId(menuId);
        orderLineItem.quantity = quantity;
        return orderLineItem;
    }

    public static OrderLineItem create(Menu menu, long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.menu = menu;
        orderLineItem.quantity = quantity;
        return orderLineItem;
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

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
