package kitchenpos.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.exception.OrderExceptionType.EMPTY_ORDER_LINE_ITEM_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_TABLE_EMPTY_EXCEPTION;
import static kitchenpos.exception.OrderTableExceptionType.ORDER_STATUS_IS_NOT_COMPLETION;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        checkOrderTableEmpty(orderTable);
        checkSize(orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        orderLineItems.forEach(this::add);
    }

    private void checkOrderTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderException(ORDER_TABLE_EMPTY_EXCEPTION);
        }
    }

    private void checkSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderException(EMPTY_ORDER_LINE_ITEM_EXCEPTION);
        }
    }

    public void add(OrderLineItem orderLineItem) {
        if (orderLineItems.contains(orderLineItem)) {
            return;
        }
        orderLineItems.add(orderLineItem);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(COMPLETION)) {
            throw new OrderException(ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION);
        }
        this.orderStatus = orderStatus;
    }

    public void changeOrderTableEmpty(boolean empty) {
        if (orderStatus == COMPLETION) {
            throw new OrderException(ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION);
        }
        orderTable.changeEmpty(empty);
    }

    public void ungroupTable() {
        if (orderStatus == COOKING || orderStatus == MEAL) {
            throw new OrderTableException(ORDER_STATUS_IS_NOT_COMPLETION);
        }

        orderTable.ungroup();
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

    public List<OrderLineItem> orderLineItems() {
        return orderLineItems;
    }
}
