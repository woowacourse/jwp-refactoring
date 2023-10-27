package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.domain.OrderStatus;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;
    private String orderStatus;
    private final LocalDateTime orderedTime = LocalDateTime.now();

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(final Long orderTableId, final String orderStatus) {
        this(null, orderTableId, orderStatus);
    }

    public Order(final Long id, final Long orderTableId, final String orderStatus) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final String orderStatus,
                 final OrderLineItems orderLineItems
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public void changeOrderStatus(final String statusName) {
        checkStatusNotCompletion();
        this.orderStatus = OrderStatus.valueOf(statusName).name();
    }

    private void checkStatusNotCompletion() {
        if (Objects.equals(OrderStatus.COMPLETION.name(), orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void addOrderLineItem(final OrderLineItem orderLineItem) {
        orderLineItems.addOrderLineItem(orderLineItem);
    }
}
