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
import javax.persistence.ManyToOne;

import kitchenpos.domain.table.OrderTable;

@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            final Long id,
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final OrderLineItems orderLineItems
    ) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static void validate(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비었어요");
        }
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

    public OrderTable getOrderTable() {
        return orderTable;
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
