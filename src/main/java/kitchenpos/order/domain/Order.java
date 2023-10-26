package kitchenpos.order.domain;

import static javax.persistence.CascadeType.PERSIST;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.exception.InvalidOrderException;
import kitchenpos.common.exception.InvalidOrderStatusException;

@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(cascade = PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems;

    @Column(name = "ordered_time", nullable = false, updatable = false)
    private LocalDateTime orderedTime;

    @Column(name = "order_table_id", insertable = false, updatable = false)
    private Long orderTableId;

    protected Order() {
    }

    public Order(final List<OrderLineItem> orderLineItems, final LocalDateTime orderedTime, final Long orderTableId) {
        this(null, OrderStatus.COOKING, orderLineItems, orderedTime, orderTableId);
    }

    public Order(final Long id, final OrderStatus orderStatus, final List<OrderLineItem> orderLineItems, final LocalDateTime orderedTime, final Long orderTableId) {
        validate(orderLineItems);
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
        this.orderedTime = orderedTime;
        this.orderTableId = orderTableId;
    }

    private void validate(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemNotEmpty(orderLineItems);
    }

    private void validateOrderLineItemNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new InvalidOrderException("주문 상품이 비어있습니다.");
        }
    }

    public void changeStatusTo(final OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new InvalidOrderStatusException("이미 완료된 주문입니다.");
        }
        if (this.orderStatus == OrderStatus.MEAL && orderStatus != OrderStatus.COMPLETION) {
            throw new InvalidOrderStatusException("식사중인 주문은 완료로만 변경할 수 있습니다.");
        }
        if (this.orderStatus == OrderStatus.COOKING && orderStatus != OrderStatus.MEAL) {
            throw new InvalidOrderStatusException("조리중인 주문은 식사중으로만 변경할 수 있습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCookingOrMeal() {
        return OrderStatus.COOKING == orderStatus || OrderStatus.MEAL == orderStatus;
    }

    public Long getId() {
        return id;
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

    public Long getOrderTableId() {
        return orderTableId;
    }
}
