package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {
    }

    private Order(
            final Long id,
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        validateEmptyByOrderTable(orderTable);
        validateEmptyByOrderLineItems(orderLineItems);
        updateOrderLineItems(orderLineItems);

        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(
            final OrderTable orderTable,
            final LocalDateTime orderedTime,
            final List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, OrderStatus.COOKING, orderedTime, orderLineItems);
    }

    private void validateEmptyByOrderTable(
            final OrderTable orderTable
    ) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("order table은 비어있을 수 없습니다.");
        }
    }

    private void validateEmptyByOrderLineItems(
            final List<OrderLineItem> orderLineItems
    ) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("order line item 은 1개 이상이어야 합니다.");
        }
    }

    public void updateOrderStatus(final String orderStatus) {
        final OrderStatus status = OrderStatus.from(orderStatus);

        if (Objects.equals(OrderStatus.COMPLETION, this.orderStatus)) {
            throw new IllegalArgumentException("주문의 상태가 COMPLETION일 때는 상태 변경이 불가 합니다.");
        }

        this.orderStatus = status;
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        orderLineItems.forEach(orderLineItem -> orderLineItem.updateOrder(this));
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

    public Optional<Long> getOrderTableId() {
        if (orderTable == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(orderTable.getId());
    }
}
