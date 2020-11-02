package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.domain.base.BaseIdEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseIdEntity {

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private kitchenpos.domain.table.Table table;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    private Order(Long id, kitchenpos.domain.table.Table table, OrderStatus orderStatus,
        LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        super(id);
        this.table = table;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        setOrder(orderLineItems);
    }

    public static Order of(Long id, kitchenpos.domain.table.Table table, OrderStatus orderStatus,
        LocalDateTime orderedTime,
        List<OrderLineItem> orderLineItems) {
        return new Order(id, table, orderStatus, orderedTime, orderLineItems);
    }

    public static Order entityOf(kitchenpos.domain.table.Table table, OrderStatus orderStatus,
        List<OrderLineItem> orderLineItems) {
        return of(null, table, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    private void setOrder(List<OrderLineItem> orderLineItems) {
        if (Objects.isNull(orderLineItems)) {
            return;
        }
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(this);
        }
    }

    public kitchenpos.domain.table.Table getTable() {
        return table;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException(
                "OrderStatus가 Completion이므로 OrderStatus를 변경할 수 없습니다.");
        }

        this.orderStatus = orderStatus;
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
