package kitchenpos.domain;

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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_table_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems;

    protected Order() {
    }

    protected Order(final OrderTable orderTable,
                    final OrderStatus orderStatus,
                    final LocalDateTime orderedTime,
                    final OrderLineItems orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    protected Order(final Long id,
                    final OrderTable orderTable,
                    final OrderStatus orderStatus,
                    final LocalDateTime orderedTime,
                    final OrderLineItems orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static Order ofEmptyOrderLineItems(final OrderTable requestOrderTable) {
        return new Order(
                requestOrderTable,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                new OrderLineItems(new ArrayList<>())
        );
    }

    public void addOrderLineItems(final List<OrderLineItem> requestOrderLineItems) {
        requestOrderLineItems.forEach(orderLineItem -> orderLineItem.assignOrder(this));
        this.orderLineItems = new OrderLineItems(requestOrderLineItems);
    }

    public void changeOrderStatus(final OrderStatus requestOrderStatus) {
        if (orderStatus == OrderStatus.COMPLETION && requestOrderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("주문 완료 상태에서 주문 완료 상태로 변경할 수 없습니다.");
        }
        this.orderStatus = requestOrderStatus;
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

    public OrderLineItems getOrderLineItems() {
        return orderLineItems;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }
}
