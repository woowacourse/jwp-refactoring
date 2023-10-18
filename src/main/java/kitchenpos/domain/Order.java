package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        validate(orderTable, orderLineItems);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }

    public static Order cooking(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, OrderStatus.COOKING, orderLineItems);
    }

    public void changeOrderStatus(OrderStatus changedOrderStatus) {
        if (isCompletion()) {
            throw new IllegalArgumentException("주문이 완료 상태면 변경할 수 없습니다.");
        }
        this.orderStatus = changedOrderStatus;
    }

    public boolean isNotCompletion() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    private void validate(OrderTable orderTable, List<OrderLineItem> orderLineItems) {

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문 받을 수 없습니다.");
        }
    }

    private boolean isCompletion() {
        return orderStatus == OrderStatus.COMPLETION;
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
        return orderLineItems.getValues();
    }
}
