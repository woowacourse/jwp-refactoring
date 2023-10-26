package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.springframework.util.CollectionUtils;

@Table(name = "orders")
@Entity
public class Order {

    private static final OrderStatus INITIAL_ORDER_STATUS = OrderStatus.COOKING;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column
    private LocalDateTime orderedTime;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected Order() {}

    public Order(final OrderTable orderTable,
                 final OrderStatus orderStatus,
                 final LocalDateTime orderedTime,
                 final List<OrderLineItem> items) {
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = items;
    }

    public static Order createDefault(final OrderTable orderTable, final LocalDateTime orderedTime, final List<OrderLineItem> items) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문하려는 테이블은 비어있을 수 없습니다.");
        }
        validateOrderLineItemsToAdd(items);

        return new Order(orderTable, INITIAL_ORDER_STATUS, orderedTime, items);
    }

    private static void validateOrderLineItemsToAdd(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 메뉴가 비어있을 수 없습니다.");
        }
    }

    public void changeOrderStatus(final OrderStatus orderStatus) {
        validateAbleToChangeOrderStatus();
        this.orderStatus = orderStatus;
    }

    private void validateAbleToChangeOrderStatus() {
        if (OrderStatus.COMPLETION == orderStatus) {
            throw new IllegalArgumentException("이미 완료된 주문이라면 주문 상태를 변경할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
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
