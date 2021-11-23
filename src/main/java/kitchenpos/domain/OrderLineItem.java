package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Order order, Menu menu, Long quantity) {
        this(null, order, menu, quantity);
    }

    private OrderLineItem(Long seq, Order order, Menu menu, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem create(Order order, Menu menu, Long quantity){
        return new OrderLineItem(order, menu, quantity);
    }

    public static OrderLineItem create(Long seq, Order order, Menu menu, Long quantity){
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getQuantity() {
        return quantity;
    }
}
