package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static kitchenpos.domain.order.OrderStatus.NOT_STARTED;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    public Order(final Long orderTableId, final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, NOT_STARTED, LocalDateTime.now(), orderLineItems);
    }

    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus,
                  final LocalDateTime orderedTime,
                  final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void place(OrderValidator orderValidator) {
        orderValidator.validate(this);
        startCooking();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus newStatus) {
        if (newStatus == OrderStatus.COOKING) {
            startCooking();
        }
        if (newStatus == OrderStatus.MEAL) {
            startMeal();
        }
        if (newStatus == OrderStatus.COMPLETION) {
            completeOrder();
        }
    }

    public void startCooking() {
        if (this.orderStatus != NOT_STARTED) {
            throw new IllegalArgumentException("이미 진행 중인 주문입니다.");
        }
        this.orderStatus = COOKING;
    }

    public void startMeal() {
        if (this.orderStatus != COOKING) {
            throw new IllegalArgumentException("조리 중인 주문이 아닙니다.");
        }
        this.orderStatus = MEAL;
    }

    public void completeOrder() {
        if (this.orderStatus != MEAL) {
            throw new IllegalArgumentException("식사 중인 주문이 아닙니다.");
        }
        this.orderStatus = COMPLETION;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
