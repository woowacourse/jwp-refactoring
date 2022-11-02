package kitchenpos.order.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                  final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public void updateOrderStatus(final OrderStatus orderStatus) {
        validateOrderNotYestCompleted();
        this.orderStatus = orderStatus;
    }

    private void validateOrderNotYestCompleted() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new IllegalArgumentException("이미 결제 완료된 주문입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
