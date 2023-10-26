package kitchenpos.domain.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table("ORDERS")
public class Order {
    @Id
    private final Long id;
    private final Long orderTableId;
    private final OrderStatus orderStatus;
    private final LocalDateTime orderedTime;
    @MappedCollection(idColumn = "ORDER_ID")
    private final Set<OrderLineItem> orderLineItems;

    private Order(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, Set<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order changeOrderStatus(OrderStatus orderStatus) {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static final class OrderBuilder {
        private Long id;
        private Long orderTableId;
        private OrderStatus orderStatus;
        private LocalDateTime orderedTime;
        private Set<OrderLineItem> orderLineItems = new HashSet<>();

        private OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder orderTableId(Long orderTableId) {
            this.orderTableId = orderTableId;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder orderedTime(LocalDateTime orderedTime) {
            this.orderedTime = orderedTime;
            return this;
        }

        public OrderBuilder orderLineItems(Set<OrderLineItem> orderLineItems) {
            this.orderLineItems = orderLineItems;
            return this;
        }

        public Order build() {
            return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
        }
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

    public Set<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
