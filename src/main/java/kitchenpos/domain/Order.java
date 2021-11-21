package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import kitchenpos.exception.CannotChangeOrderStatus;
import kitchenpos.exception.InvalidOrderException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        validateOrderLineItemNotEmpty(orderLineItems);
        validateOrderTableNotEmpty(orderTable);
        this.orderTable = orderTable;
        this.orderStatus = OrderStatus.COOKING;
        this.orderedTime = LocalDateTime.now();
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemNotEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderException();
        }
    }

    private void validateOrderTableNotEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderException();
        }
    }

    public boolean isNotCompletion() {
        return orderStatus.isNotCompletion();
    }

    public void changeOrderStatus(String orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new CannotChangeOrderStatus();
        }

        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public String getOrderStatus() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
