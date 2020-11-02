package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private OrderLineItemQuantity orderLineItemQuantity;

    public OrderLineItem() {
    }

    public OrderLineItem(Order order, Long menuId, Long quantity) {
        this(null, order, menuId, quantity);
    }

    public OrderLineItem(Long seq, Order order, Long menuId, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.orderLineItemQuantity = new OrderLineItemQuantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public OrderLineItemQuantity getOrderLineItemQuantity() {
        return orderLineItemQuantity;
    }

    public void changeOrderId(Order order) {
        this.order = order;
    }
}
