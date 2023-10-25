package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "ORDERS")
public class Order extends AbstractAggregateRoot<Order> {
    @Id
    private Long id;
    @Column("ORDER_TABLE_ID")
    private AggregateReference<OrderTable, Long> orderTable;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @MappedCollection(idColumn = "ORDER_ID")
    private Set<OrderLineItem> orderLineItems = new HashSet<>();

    public Order(Long orderTableId, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(Long orderTableId, OrderStatus orderStatus, List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, orderStatus, LocalDateTime.now(), orderLineItems);
    }

    @PersistenceCreator
    public Order(Long id, Long orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.id = id;
        this.orderTable = AggregateReference.to(orderTable);
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new HashSet<>(orderLineItems);
    }

    public static Order createWithoutId(Long orderTableId, List<OrderLineItem> orderLineItems, OrderValidator orderValidator) {
        Order order = new Order(orderTableId, orderLineItems);
        orderValidator.validate(order);
        return order;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("완료 상태의 주문은 변경할 수 없습니다");
        }
        this.orderStatus = orderStatus;
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 하나 이상이여야 합니다");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return new ArrayList<>(orderLineItems);
    }
}
