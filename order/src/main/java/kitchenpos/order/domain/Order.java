package kitchenpos.order.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.order.exception.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.order.exception.OrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(Long orderTableId, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            Long id,
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            OrderLineItems orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public boolean isCookingOrMeal() {
        return orderStatus == COOKING || orderStatus == MEAL;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == COMPLETION) {
            throw new OrderException(CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS);
        }
        this.orderStatus = orderStatus;
    }

    public Long id() {
        return id;
    }

    public Long orderTableId() {
        return orderTableId;
    }

    public OrderStatus orderStatus() {
        return orderStatus;
    }

    public LocalDateTime orderedTime() {
        return orderedTime;
    }

    public OrderLineItems orderLineItems() {
        return orderLineItems;
    }
}
