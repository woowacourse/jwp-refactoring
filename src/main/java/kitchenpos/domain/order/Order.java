package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Entity
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this.id = null;
        this.orderTable = orderTable;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        validateOrderTable(orderTable);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있으면 생성할 수 없다.");
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("이미 완료된 주문은 변경할 수 없다.");
        }
        this.orderStatus = orderStatus;
    }

    public boolean isCookingOrMeal() {
        return orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getCollection();
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new OrderLineItems(orderLineItems);
    }
}
