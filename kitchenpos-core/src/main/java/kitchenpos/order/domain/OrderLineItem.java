package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Long seq;

    @Column(name = "order_id", length = 20)
    private Long orderId;

    @Column(name = "menu_id", length = 20, nullable = false)
    private Long menuId;

    @Column(length = 20, nullable = false)
    private long quantity;

    public OrderLineItem(final Long seq, final Long orderId, final Long menuId, final long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem(final Long orderId, final Long menuId, final long quantity) {
        this(null, orderId, menuId, quantity);
    }

    public OrderLineItem(final Long menuId, final long quantity) {
        this(null, null, menuId, quantity);
    }

    protected OrderLineItem() {
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLineItem orderLineItem)) {
            return false;
        }
        return Objects.equals(seq, orderLineItem.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
