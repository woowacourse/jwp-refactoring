package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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

@Table(name = "orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<OrderLineItem> orderLineItems;

    protected Order() {
    }

    public Order(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        this(null, orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    public Order(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime, new ArrayList<>());
    }

    public Order(Long id, OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        validateOrderTable(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블입니다");
        }
    }

    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목은 하나 이상이여야 합니다");
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

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public void changeOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus == OrderStatus.COMPLETION) {
            throw new IllegalArgumentException("완료 상태의 주문은 변경할 수 없습니다");
        }
        this.orderStatus = orderStatus;
    }
}
