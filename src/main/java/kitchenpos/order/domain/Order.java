package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("ORDERS")
public class Order {
    @Id
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @MappedCollection(idColumn = "ORDER_ID")
    private Set<OrderLineItem> orderLineItems;

    @PersistenceCreator
    public Order(final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new HashSet<>(orderLineItems);
    }

    // FIXME : 이거 테스트에서만 사용중..
    public Order(final Long id, final Long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new HashSet<>(orderLineItems);
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

    public void transitionToNextStatus() {
        this.orderStatus = orderStatus.transitionToNextStatus();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }
}
