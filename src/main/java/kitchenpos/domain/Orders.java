package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @OneToMany(mappedBy = "orders")
    private List<OrderLineItem> orderLineItems;

    public Orders() {
    }

    public Orders(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, null, null, orderLineItems);
    }

    public Orders(
            OrderTable orderTable,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, null, orderedTime, orderLineItems);
    }

    public Orders(
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this(null, null, orderStatus, orderedTime, orderLineItems);
    }

    public Orders(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Orders(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Orders of(
            OrderStatus orderStatus,
            List<OrderLineItem> orderLineItems
    ) {
        return new Orders(orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public void registerOrderTable(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException();
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
