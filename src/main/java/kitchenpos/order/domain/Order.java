package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long orderTableId;

    @Enumerated(STRING)
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime;

//    @OneToMany(mappedBy = "order", cascade = PERSIST)
//    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(Long orderTableId, LocalDateTime orderedTime) {
        this.orderTableId = orderTableId;
        this.orderedTime = orderedTime;
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

//    public void addOrderLineItem(OrderLineItem orderLineItem) {
//        this.orderLineItems.add(orderLineItem);
//        orderLineItem.changeOrder(this);
//    }

//    public void checkEqualMenuCount(long orderMenuSize) {
//        if (orderMenuSize != orderLineItems.size()) {
//            throw new IllegalArgumentException();
//        }
//    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

//    public List<OrderLineItem> getOrderLineItems() {
//        return orderLineItems;
//    }
}
