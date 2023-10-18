package kitchenpos.order.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private OrderTable orderTable;

    private String orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(
            OrderTable orderTable,
            List<OrderLineItem> orderLineItems,
            OrderValidator orderValidator
    ) {
        orderValidator.validateCreate(orderLineItems, orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING.name();
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(final String orderStatus) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), this.orderStatus)) {
            throw new OrderException("이미 결제 완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }


    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
