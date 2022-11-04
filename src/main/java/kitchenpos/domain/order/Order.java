package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.event.Events;
import kitchenpos.domain.order.event.OrderStatusChangedEvent;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_table_id")
    private Long orderTableId;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime) {
        this(id, orderTableId, orderStatus, orderedTime, null);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus,
                 final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        Events.publishEvent(new OrderStatusChangedEvent(orderTableId, COOKING.name()));
    }

    private void validateOrderLineItems(final OrderLineItems orderLineItems) {
        if (orderLineItems.getOrderLineItems().size() < 1) {
            throw new IllegalArgumentException();
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new IllegalArgumentException("이미 주문의 상태가 완료입니다.");
        }
        Events.publishEvent(new OrderStatusChangedEvent(this.orderTableId, orderStatus.name()));
        this.orderStatus = orderStatus.name();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
