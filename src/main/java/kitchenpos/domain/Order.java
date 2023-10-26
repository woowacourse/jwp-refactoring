package kitchenpos.domain;

import kitchenpos.application.exception.InvalidOrderStatusToChangeException;
import kitchenpos.domain.exception.InvalidOrderLineItemsToOrder;
import kitchenpos.domain.exception.InvalidOrderTableToOrder;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", foreignKey = @ForeignKey(name = "fk_order_order_table"))
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> orderLineItems;

    protected Order() {}

    private Order(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime now,
            final List<OrderLineItem> orderLineItems
    ) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = now;
        this.orderLineItems = orderLineItems;
    }

    public static Order of(
            final OrderTable orderTable,
            final OrderStatus orderStatus,
            final LocalDateTime now,
            final List<OrderLineItem> orderLineItems
    ) {
        validateOrderTable(orderTable);
        validateOrderLineItems(orderLineItems);

        final Order order = new Order(orderTable, orderStatus, now, orderLineItems);
        addOrderLineItems(orderLineItems, order);

        return order;
    }

    private static void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidOrderTableToOrder("주문 테이블이 비어 있어 주문이 불가능합니다.");
        }
    }

    private static void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new InvalidOrderLineItemsToOrder("주문 항목이 없습니다.");
        }
    }

    private static void addOrderLineItems(final List<OrderLineItem> orderLineItems, final Order order) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
        }
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

    public void updateOrderStatus(final OrderStatus orderStatus) {
        validateOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateOrderStatus() {
        if (Objects.equals(OrderStatus.COMPLETION, orderStatus)) {
            throw new InvalidOrderStatusToChangeException("주문이 상태가 계산 완료라면 상태를 변경할 수 없다.");
        }
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", orderStatus='" + orderStatus + '\'' +
               ", orderedTime=" + orderedTime +
               '}';
    }
}
