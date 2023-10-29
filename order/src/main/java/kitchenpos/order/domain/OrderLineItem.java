package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private long quantity;
    @Embedded
    private OrderedMenu orderedMenu;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order,
                         final long quantity,
                         final OrderedMenu orderedMenu) {
        this(null, order, quantity, orderedMenu);
    }

    public OrderLineItem(final Long seq,
                         final Order order,
                         final long quantity,
                         final OrderedMenu orderedMenu) {
        this.seq = seq;
        this.order = order;
        this.quantity = quantity;
        this.orderedMenu = orderedMenu;
    }

    public void setOrder(final Order order) {
        this.order = order;
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

    public OrderedMenu getOrderedMenu() {
        return orderedMenu;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "seq=" + seq +
                ", orderId=" + order.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
