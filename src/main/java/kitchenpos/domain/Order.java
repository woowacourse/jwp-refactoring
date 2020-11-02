package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private kitchenpos.domain.Table table;
    private String orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(Long id, kitchenpos.domain.Table table, String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.table = table;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        setOrder(orderLineItems);
    }

    public static Order of(Long id, kitchenpos.domain.Table table, String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        return new Order(id, table, orderStatus, orderedTime, orderLineItems);
    }

    public static Order entityOf(kitchenpos.domain.Table table, String orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        return new Order(null, table, orderStatus, orderedTime, orderLineItems);
    }

    private void setOrder(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            return;
        }
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    public Long getId() {
        return id;
    }

    public kitchenpos.domain.Table getTable() {
        return table;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", orderStatus='" + orderStatus + '\'' +
            ", orderedTime=" + orderedTime +
            '}';
    }
}
