package kitchenpos.domain.order;

import static kitchenpos.domain.vo.OrderStatus.COOKING;
import static kitchenpos.domain.vo.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import kitchenpos.domain.vo.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false, table = "orderTable", name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_to_order_table"))
    private long orderTableId;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(final long orderTableId, final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = COOKING;
        orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(this));
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = orderStatus;
    }

    public boolean isInProgress() {
        return MEAL.equals(orderStatus) || COOKING.equals(orderStatus);
    }

    public Long getId() {
        return id;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems.getOrderLineItems());
    }
}
