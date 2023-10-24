package kitchenpos.domain;

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
    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order,
                         final Menu menu,
                         final long quantity) {
        this(null, order, menu, quantity);
    }

    public OrderLineItem(final Long seq,
                         final Order order,
                         final Menu menu,
                         final long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void dependOn(final Order order) {
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
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
                ", menuId=" + menu.getId() +
                ", quantity=" + quantity +
                '}';
    }
}
