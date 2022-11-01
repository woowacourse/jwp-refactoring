package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Column;
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
    @Column(name = "menu_id", nullable = false)
    private Long menuId;
    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, new Quantity(quantity));
    }

    public OrderLineItem(final Long seq, final Order order, final Long menuId, final Quantity quantity) {
        this.seq = seq;
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
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

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity.getValue();
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
