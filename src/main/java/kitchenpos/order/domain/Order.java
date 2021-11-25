package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.order.exception.OrderAlreadyCompletionException;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderLineItems orderLineItems;

    @NotNull
    @JoinColumn(name = "order_table_id")
    private Long orderTableId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @NotNull
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(Long orderTableId) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now());
    }

    public Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        validateNull(this.orderTableId);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidOrderException("Order 정보에 null이 포함되었습니다.");
        }
    }

    public void addOrderLineItems(OrderLineItems orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new OrderAlreadyCompletionException(String.format("%s ID Order는 이미 완료 상태입니다.", id));
        }

        this.orderStatus = orderStatus;
    }

    private boolean isCompletion() {
        return orderStatus.equals(COMPLETION);
    }

    public boolean isNotCompletion() {
        return !isCompletion();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
