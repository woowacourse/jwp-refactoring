package kitchenpos.domain.order;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import kitchenpos.application.order.OrderValidator;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id", nullable = false)
    private Long orderTableId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final Long orderTableId, final OrderLineItems orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            final Long id,
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final OrderLineItems orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final Long orderTableId,
                           final OrderLineItems orderLineItems,
                           final OrderValidator orderValidator
    ) {
        orderValidator.validate(orderTableId, orderLineItems);
        return new Order(orderTableId, orderLineItems);
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.equals(COMPLETION)) {
            throw new IllegalArgumentException("더 이상 상태를 변경할 수 없습니다");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isNotCompletionStatus() {
        return !orderStatus.equals(COMPLETION);
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
