package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.badrequest.OrderAlreadyCompletedException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_table_id")
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @CreatedDate
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(this, orderLineItems);
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, null, orderLineItems);
    }

    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public boolean isComplete() {
        return this.orderStatus == OrderStatus.COMPLETION;
    }

    public Order changeOrderStatus(final OrderStatus orderStatus) {
        if (isComplete()) {
            throw new OrderAlreadyCompletedException(this.id);
        }
        this.orderStatus = orderStatus;

        return this;
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
