package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime,
                 OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
        this.orderLineItems.setOrder(this);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, LocalDateTime.now(), new OrderLineItems(orderLineItems));
    }

    public static Order first(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블입니다.");
        }
        return new Order(orderTable.getId(), OrderStatus.COOKING, orderLineItems);
    }

    public void changeStatus(OrderStatus status) {
        if (status.isCompletion()) {
            throw new IllegalArgumentException("계산 완료된 주문입니다.");
        }
        this.orderStatus = status;
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }
}
