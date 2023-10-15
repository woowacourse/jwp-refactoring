package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.BaseEntity;

@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블인 경우 주문을 할 수 없습니다.");
        }
    }

    public void validateUngroupTableAllowed() {
        if (orderStatus != COMPLETION) {
            throw new IllegalArgumentException("테이블의 주문 상태가 조리중이거나 식사중인 경우 단체 지정 해제를 할 수 없습니다.");
        }
    }

    public void validateChangeTableStatusAllowed() {
        if (orderStatus != COMPLETION) {
            throw new IllegalArgumentException("테이블의 주문 상태가 조리중이거나 식사중인 경우 테이블의 상태를 변경할 수 없습니다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == COMPLETION) {
            throw new IllegalArgumentException("완료된 주문의 상태는 변경할 수 없습니다.");
        }
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
