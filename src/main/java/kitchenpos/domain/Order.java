package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "orders")
public class Order {

    private static final int MINIMUM_ORDER_LINE_ITEM_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_table_id", nullable = false)
    private long orderTableId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(final long orderTableId, final OrderStatus orderStatus) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public void changeOrderStatus(final String orderStatusName) {
        if (orderStatus.isComplete()) {
            throw new IllegalArgumentException();
        }
        this.orderStatus = OrderStatus.find(orderStatusName)
                                      .orElseThrow(() -> new IllegalArgumentException("잘못된 상태입니다."));
    }

    public void addOrderItems(final List<OrderLineItem> savedOrderLineItems) {
        if (savedOrderLineItems.size() < MINIMUM_ORDER_LINE_ITEM_SIZE) {
            throw new IllegalArgumentException();
        }
        this.orderLineItems = savedOrderLineItems;
    }

    public Long getId() {
        return id;
    }

    public long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public String getOrderStatus() {
        return orderStatus.getName();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
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
}
