package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "order_menu_id")
    private OrderMenu orderMenu;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Long orderId, final OrderMenu orderMenu, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
