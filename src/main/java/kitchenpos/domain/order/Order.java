package kitchenpos.domain.order;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.domain.OrderStatus.valueOf;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

@Entity(name = "Orders")
public class Order {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime orderedTime;

    @OneToMany(cascade = PERSIST, fetch = EAGER)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private List<OrderLineItem> orderLineItems;

    Order(
            Long id,
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    Order(
            OrderTable orderTable,
            OrderStatus orderStatus,
            LocalDateTime orderedTime,
            List<OrderLineItem> orderLineItems
    ) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    protected Order() {
    }

    public static Order of(OrderTable orderTable, List<OrderLineItem> orderLineItems, long menuCount) {
        validateOrderLineItemsIsEmpty(orderLineItems);
        validateOrderLineItemsSizeWithMenuCount(orderLineItems, menuCount);
        return new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems);
    }

    private static void validateOrderLineItemsIsEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 입력되지 않았습니다.");
        }
    }

    private static void validateOrderLineItemsSizeWithMenuCount(List<OrderLineItem> orderLineItems, long menuCount) {
        if (orderLineItems.size() != menuCount) {
            throw new IllegalArgumentException("메뉴에 없는 항목을 주문할 수 없습니다.");
        }
    }

    public void register(OrderTable orderTable) {
        this.orderTable = orderTable;
        if (!orderTable.contains(this)) {
            orderTable.add(this);
        }
    }

    public void changeOrderStatus(String orderStatus) {
        validateOrderStatusCanBeChanged();
        this.orderStatus = valueOf(orderStatus);
    }

    private void validateOrderStatusCanBeChanged() {
        if (Objects.equals(COMPLETION, orderStatus)) {
            throw new IllegalArgumentException("계산 완료된 주문은 주문 상태를 변경할 수 없습니다.");
        }
    }

    public boolean isCookingOrMealStatus() {
        return (orderStatus == MEAL) || (orderStatus == COOKING);
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getOrderStatusValue() {
        return orderStatus.name();
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
