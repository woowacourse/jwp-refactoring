package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderTableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(
            Long orderTableId,
            LocalDateTime orderedTime
    ) {
        this(null, orderTableId, COOKING, orderedTime);
    }

    private Order(
            Long id,
            Long orderTableId,
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Order of(
            Long orderTableId,
            List<OrderLineItem> orderLineItems
    ) {
        Order order = new Order(orderTableId, LocalDateTime.now());
        order.addAllOrderLineItems(orderLineItems);

        return order;
    }

    public void registerOrderTable(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void addAllOrderLineItems(List<OrderLineItem> orderLineItems) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.registerOrders(this));
        this.orderLineItems.addAll(orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("이미 완료된 주문의 상태를 바꿀 수 없습니다.");
        }

        this.orderStatus = orderStatus;
    }

    public boolean isCompletion() {
        return COMPLETION.equals(orderStatus);
    }

    public boolean isCooking() {
        return COOKING.equals(orderStatus);
    }

    public boolean isMeal() {
        return MEAL.equals(orderStatus);
    }

    public boolean isCookingOrMeal() {
        return isCooking() || isMeal();
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

}
