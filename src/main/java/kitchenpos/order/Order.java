package kitchenpos.order;

import kitchenpos.domain.table.OrderTable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderTableId;
    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column
    private LocalDateTime orderedTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public Order() {
    }

    public Order(final Long id,
                 final Long orderTableId,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems.addAll(orderLineItems);
    }

    public Order(final Long orderTableId,
                 final String orderStatusName,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> orderLineItems) {
        this(null, orderTableId, OrderStatus.valueOf(orderStatusName), orderedTime, orderLineItems);
    }

    public Order(final OrderTable orderTable,
                 final String orderStatus,
                 final LocalDateTime orderedTime) {
        this(null, orderTable.getId(), OrderStatus.valueOf(orderStatus), orderedTime, List.of());
    }

    public void addOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems.addAll(orderLineItems);
    }

    public void place(final OrderValidator orderValidator) {
        orderValidator.validate(this);
    }

    public void updateStatus(final Order other) {
        validateStatus();
        this.orderStatus = other.orderStatus;
    }

    private void validateStatus() {
        if (OrderStatus.COMPLETION == this.orderStatus) {
            throw new IllegalArgumentException("주문이 이미 완료되었습니다. 주문상태를 변경할 수 없습니다.");
        }
    }

    public boolean isStatus(final OrderStatus orderStatus) {
        return this.orderStatus == orderStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
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
