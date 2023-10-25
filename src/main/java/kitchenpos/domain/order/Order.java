package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;

@Entity
@Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    @Embedded
    private OrderLineItems orderLineItems = new OrderLineItems();

    protected Order() {
    }

    public Order(final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this(null, null, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    public Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final OrderLineItems orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public void addAllOrderLineItems(final OrderLineItems orderLineItems) {
        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            orderLineItem.changeOrder(this);
            this.orderLineItems.add(orderLineItem);
        }
    }

    public void changeOrderStatus(final OrderStatus toChangeOrderStatus) {
        if (COMPLETION == orderStatus) {
            throw new IllegalArgumentException(
                    String.format(
                            "주문 상태가 완료인 상태에서 주문 상태를 변경할 수 없습니다. from = %s, to = %s",
                            orderStatus,
                            toChangeOrderStatus
                    ));
        }

        this.orderStatus = toChangeOrderStatus;
    }

    public boolean isProceeding() {
        return orderStatus == OrderStatus.COOKING || orderStatus == OrderStatus.MEAL;
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
}
