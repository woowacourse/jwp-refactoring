package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends BaseIdEntity {

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
        super(id);
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

    public kitchenpos.domain.Table getTable() {
        return table;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException(
                "OrderStatus가 Completion이므로 OrderStatus를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderStatus='" + orderStatus + '\'' +
            ", orderedTime=" + orderedTime +
            '}';
    }
}
