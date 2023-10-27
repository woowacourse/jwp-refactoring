package kitchenpos.domain.order;

import kitchenpos.common.BaseDate;
import kitchenpos.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "orders")
public class Order extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_table_id")
    private OrderTable orderTable;

    @Enumerated(STRING)
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    private Order(final Long id, final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        validateEmptyOrderTable(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("[ERROR] 주문할 아이템을 최소 1개 이상 선택해 주세요!");
        }

        final int distinctMenuIdNumber = (int) orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .distinct().count();

        if (orderLineItems.size() != distinctMenuIdNumber) {
            throw new IllegalArgumentException("[ERROR] 주문할 아이템이 같은 메뉴라면, 수량으로 개수를 표시해 주세요.");
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블에서는 주문을 할 수 없습니다.");
        }
    }

    public Order(final OrderTable orderTable, final OrderStatus orderStatus, final LocalDateTime orderedTime, final List<OrderLineItem> orderLineItems) {
        this(null, orderTable, orderStatus, orderedTime, orderLineItems);
    }

    protected Order() {
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

    public void changeOrderStatus(final OrderStatus orderStatus) {
        if (!canChangeOrderStatus()) {
            throw new IllegalStateException("[ERROR] 이미 완료된 주문은 상태를 변경할 수 없습니다.");
        }
        this.orderStatus = orderStatus;
    }

    private boolean canChangeOrderStatus() {
        return orderStatus != OrderStatus.COMPLETION;
    }

    public void updateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
