package kitchenpos.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.InvalidOrderException;
import kitchenpos.exception.OrderTableUpdateException;

@Entity(name = "orders")
public class Order {

    public static final List<String> INVALID_STATUS_WHEN_UPDATE_ORDER_TABLE = List.of(OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    private String orderStatus;

    private LocalDateTime orderedTime;
    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long orderTableId, final String orderStatus, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus,
                 final List<OrderLineItem> orderLineItems) {
        this(id, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime.truncatedTo(ChronoUnit.MICROS);
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order of(final Long orderTableId, final List<OrderLineItem> orderLineItems,
                           final OrderValidator orderValidator) {
        final Order order = new Order(orderTableId, orderLineItems);
        orderValidator.validate(order);
        return order;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new InvalidOrderException("주문 항목 리스트는 비어있을 수 없습니다.");
        }
    }

    public void changeStatus(final OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.COMPLETION.name())) {
            throw new InvalidOrderException("주문 상태가 \"계산 완료\"이면 주문 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus.name();
    }

    public void validatePossibleToUpdateOrderTable() {
        if (INVALID_STATUS_WHEN_UPDATE_ORDER_TABLE.contains(this.orderStatus)) {
            throw new OrderTableUpdateException("조리중 or 식사중인 주문이 포함되어 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }
}
