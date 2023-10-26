package kitchenpos.domain.order;

import kitchenpos.domain.table.OrderTable;

import javax.persistence.CascadeType;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;
    //    private Long orderTableId;
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
                 final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
    }

    public Order(Long id,
                 OrderTable orderTable,
                 OrderStatus orderStatus,
                 LocalDateTime orderedTime,
                 List<OrderLineItem> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public Order(final OrderTable orderTable,
                 final String orderStatus,
                 final LocalDateTime orderedTime) {
        this(null, orderTable, OrderStatus.valueOf(orderStatus), orderedTime);
    }

    public void addOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalStateException("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }
        this.orderLineItems.addAll(orderLineItems);
    }

//    public void place(OrderValidator orderValidator) {
//        orderValidator.validate(this);
//        this.orderStatus = OrderStatus.Ordered;
//    }

    public void updateStatus(final Order order) {
        validateStatus();
        this.orderStatus = order.orderStatus;
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

    public OrderTable getOrderTable() {
        return orderTable;
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
