package kitchenpos.order.domain;

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

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time", nullable = false)
    private LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems.join(this);
    }

    public Order(final Long orderTableId, final OrderLineItems orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    public void changeStatus(final String changingStatus) {
        if (orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("이미 Completion인 상태인 경우 주문 테이블의 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = OrderStatus.valueOf(changingStatus);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
