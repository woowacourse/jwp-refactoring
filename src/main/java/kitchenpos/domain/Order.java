package kitchenpos.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.exception.OrderExceptionType.CAN_NOT_CHANGE_COMPLETION_ORDER_STATUS;
import static kitchenpos.exception.OrderExceptionType.ORDER_LINE_ITEMS_CAN_NOT_EMPTY;
import static kitchenpos.exception.OrderExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.OrderException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        validate(orderTable, orderLineItems);
        orderLineItems.forEach(it -> it.setOrder(this));
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        if (orderTable.empty()) {
            throw new OrderException(ORDER_TABLE_CAN_NOT_EMPTY);
        }
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new OrderException(ORDER_LINE_ITEMS_CAN_NOT_EMPTY);
        }
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

    public List<OrderLineItem> orderLineItems() {
        return orderLineItems;
    }
}
