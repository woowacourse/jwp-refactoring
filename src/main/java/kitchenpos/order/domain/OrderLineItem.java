package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Order 에 속하는 수량이 있는 Menu
 */
@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "menu_order_id")
    private Long menuOrderId;

    @Column(name = "quantity")
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Long orderId, final Long orderMenuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuOrderId = orderMenuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderMenuId, final long quantity) {
        this(null, null, orderMenuId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuOrderId() {
        return menuOrderId;
    }

    public long getQuantity() {
        return quantity;
    }
}
