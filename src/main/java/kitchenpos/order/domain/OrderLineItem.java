package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_menu_id", nullable = false)
    private OrderMenu orderMenu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final Order order, final OrderMenu orderMenu, final long quantity) {
        this.seq = seq;
        this.order = order;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public void updateOrder(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
