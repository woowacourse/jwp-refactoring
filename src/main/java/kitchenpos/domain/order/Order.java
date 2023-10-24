package kitchenpos.domain.order;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static kitchenpos.exception.order.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;
import static kitchenpos.exception.order.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.order.OrderException;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        this(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            OrderLineItems orderLineItems
    ) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.empty()) {
            throw new OrderException(ORDER_TABLE_CAN_NOT_EMPTY);
        }
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

    public OrderTable orderTable() {
        return orderTable;
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
