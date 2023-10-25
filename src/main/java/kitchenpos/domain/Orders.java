package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Orders() {
    }

    private Orders(
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        this(null, null, orderStatus, orderedTime);
    }

    private Orders(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public static Orders of(
            OrderStatus orderStatus,
            List<OrderLineItem> orderLineItems
    ) {
        Orders orders = new Orders(orderStatus, LocalDateTime.now());
        orders.addAllOrderLineItems(orderLineItems);

        return orders;
    }

    public void registerOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

}
