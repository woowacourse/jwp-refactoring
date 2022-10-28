package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Menu menu;

    private long quantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Order getOrder() {
        return order;
    }

//    public void setOrderId(final Long orderId) {
//        this.orderId = orderId;
//    }

    public Menu getMenu() {
        return menu;
    }

//    public void setMenuId(final Long menuId) {
//        this.menuId = menuId;
//    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
