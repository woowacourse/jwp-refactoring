package kitchenpos.domain.order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    public static final String CHANGE_UNORDERABLE_WHEN_STATUS_IN_COOKING_OR_MEAL_ERROR_MESSAGE = "주문 상태가 조리중이거나 식사중인 경우 테이블 상태를 변경할 수 없습니다.";
    public static final String CHANGE_COMPLETED_ORDER_STATUS_ERROR_MESSAGE = "이미 완료된 주문은 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    private Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(final OrderTable orderTable, final OrderLineItems orderLineItems) {
        return new Order(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public void setUnOrderable() {
        if (this.orderStatus == OrderStatus.COOKING || this.orderStatus == OrderStatus.MEAL) {
            throw new IllegalArgumentException(CHANGE_UNORDERABLE_WHEN_STATUS_IN_COOKING_OR_MEAL_ERROR_MESSAGE);
        }
    }

    public boolean isNotCompleted() {
        return this.orderStatus != OrderStatus.COMPLETION;
    }

    public void setOrderStatus(final OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException(CHANGE_COMPLETED_ORDER_STATUS_ERROR_MESSAGE);
        }
        this.orderStatus = orderStatus;
    }

    public Order(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
