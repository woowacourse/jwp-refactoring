package kitchenpos.order.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.type.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.type.OrderStatus.COOKING;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidOrderStateException;
import kitchenpos.order.domain.type.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orderId", fetch = LAZY, cascade = ALL)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public void validateIsNotComplete() {
        if (!orderStatus.equals(COMPLETION)) {
            throw new InvalidOrderStateException("조리 중이거나 식사 중인 테이블의 상태는 변경 할 수 없습니다.");
        }
    }

    public void validateIsComplete() {
        if (orderStatus.equals(COMPLETION)) {
            throw new InvalidOrderStateException("계산이 완료된 주문의 상태를 변경할 수 없습니다.");
        }
    }

    private Order(
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(
            final Long id,
            final Long orderTableId,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this(orderTableId, orderStatus, orderedTime);
        this.id = id;
        this.orderLineItems = orderLineItems;
    }

    public static Order from(final Long orderTableId) {
        return new Order(orderTableId, COOKING, LocalDateTime.now());
    }

    public static Order of(final Order order, final List<OrderLineItem> orderLineItems) {
        return new Order(order.id, order.orderTableId, order.orderStatus, order.getOrderedTime(), orderLineItems);
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTable() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
