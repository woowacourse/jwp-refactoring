package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_menu_id", nullable = false)
    private OrderMenu orderMenu;

    @Column
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long seq, final OrderMenu orderMenu, final long quantity) {
        this.seq = seq;
        this.orderMenu = orderMenu;
        this.quantity = quantity;
    }

    public OrderLineItem(final OrderMenu orderMenu, final long quantity) {
        this(null, orderMenu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public OrderMenu getOrderMenu() {
        return orderMenu;
    }

    public long getQuantity() {
        return quantity;
    }
}
