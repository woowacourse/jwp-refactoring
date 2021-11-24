package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private Long menuId;

    @Column
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Order order, Long menuId, Long quantity) {
        this(null, order, menuId, quantity);
    }

    private OrderLineItem(Long seq, Order order, Long menuId, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItem create(Order order, Long menuId, Long quantity) {
        return new OrderLineItem(order, menuId, quantity);
    }

    public static OrderLineItem create(Long seq, Order order, Long menuId, Long quantity) {
        return new OrderLineItem(seq, order, menuId, quantity);
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

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
