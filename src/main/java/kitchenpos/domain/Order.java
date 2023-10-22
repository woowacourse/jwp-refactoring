package kitchenpos.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;

@Table(name = "orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems;

    public Order() {
    }

    private Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        // TODO: orderLineItems의 orderId 변경하기
        // TODO: orderLineItems의 menuId 변경하기
        validateOrderLineItemsSize(orderLineItems.size());
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private Order(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, COOKING, LocalDateTime.now(), orderLineItems);
    }

    public static Order create(final OrderTable orderTable, final List<OrderLineItem> orderLineItems) {
        return new Order(orderTable, orderLineItems);
    }

    private void validateOrderLineItemsSize(final int orderLineItemsSize) {
        if (orderLineItemsSize < 1) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public void setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        // TODO: meal -> cooking의 상태를 가능하게 할 것인가?
        if (COMPLETION.equals(this.orderStatus)) {
            throw new IllegalArgumentException("이미 완료된 주문은 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
        this.orderedTime = LocalDateTime.now();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(final LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void setOrderLineItems(final List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }
}
