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

    private Long menuId;

    private long quantity;

    protected OrderLineItem() {
    }

    public static OrderLineItem create(long id, long orderId, long menuId, long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.seq = id;
        orderLineItem.order = Order.createSingleId(orderId);
        orderLineItem.menuId = menuId;
        orderLineItem.quantity = quantity;
        return orderLineItem;
    }

    public static OrderLineItem create(Long menuId, long quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.menuId = menuId;
        orderLineItem.quantity = quantity;
        return orderLineItem;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
