package kitchenpos.order.domain;

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
import kitchenpos.order.domain.vo.MenuInfo;

@Entity
public class OrderLineItem {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    @Id
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private MenuInfo menuInfo;

    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order,
                         final MenuInfo menuInfo,
                         final long quantity) {
        this(null, order, menuInfo, quantity);
    }

    public OrderLineItem(final Long id,
                         final Order order,
                         final MenuInfo menuInfo,
                         final long quantity) {
        this.id = id;
        this.order = order;
        this.menuInfo = menuInfo;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "id=" + id +
                ", orderId=" + order.getId() +
                ", menuInfo=" + menuInfo +
                ", quantity=" + quantity +
                '}';
    }
}
