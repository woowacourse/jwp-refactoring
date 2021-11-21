package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.NotChangeCompletionStatusException;
import kitchenpos.exception.OrderEmptyException;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    public Order() {
    }

    public Order(OrderBuilder orderBuilder) {
        this.id = orderBuilder.id;
        this.orderTable = orderBuilder.orderTable;
        this.orderStatus = orderBuilder.orderStatus;
        this.orderedTime = orderBuilder.orderedTime;
        changeOrderLineItems(orderBuilder.orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.isCompletion()) {
            throw new NotChangeCompletionStatusException();
        }

        this.orderStatus = orderStatus;
    }

    public boolean isEmptyOrderLineItems() {
        return orderLineItems.isEmpty();
    }

    public void changeOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderEmptyException();
        }

        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(this);
        }

        this.orderLineItems = OrderLineItems.create(orderLineItems);
    }

    public boolean isNotCompletion() {
        return !orderStatus.isCompletion();
    }

    public Long getId() {
        return id;
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

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems.getOrderLineItems();
    }

    public static class OrderBuilder {

        private Long id;
        private OrderTable orderTable;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private List<OrderLineItem> orderLineItems;

        public OrderBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder setOrderTable(OrderTable orderTable) {
            this.orderTable = orderTable;
            return this;
        }

        public OrderBuilder setOrderStatus(String orderStatus) {
            this.orderStatus = OrderStatus.findByString(orderStatus);
            return this;
        }

        public OrderBuilder setOrderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder setOrderLineItems(List<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
