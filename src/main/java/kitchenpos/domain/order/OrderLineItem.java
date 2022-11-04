package kitchenpos.domain.order;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.common.Quantity;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Embedded
    private Quantity quantity;
    @Embedded
    private OrderedMenu orderedMenu;

    protected OrderLineItem() {
    }

    public OrderLineItem(final long quantity, final OrderedMenu orderedMenu) {
        this(null, null, new Quantity(quantity), orderedMenu);
    }

    public OrderLineItem(final Long seq, final Order order, final Quantity quantity, final OrderedMenu orderedMenu) {
        this.seq = seq;
        this.order = order;
        this.quantity = quantity;
        this.orderedMenu = orderedMenu;
    }

    public void mapOrder(final Order order) {
        if (this.order != null) {
            this.order.getOrderLineItems()
                    .remove(this);
        }
        this.order = order;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public long getQuantity() {
        return quantity.getValue();
    }

    public String getMenuName() {
        return orderedMenu.getName();
    }

    public BigDecimal getMenuPrice() {
        return orderedMenu.getPrice();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLineItem)) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
